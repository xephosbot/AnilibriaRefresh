package com.xbot.title.screen.details

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.common.AsyncResult
import com.xbot.common.getOrNull
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.MoreVert
import com.xbot.designsystem.icons.Star
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.theme.AnilibertyTheme
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.domain.fixtures.createReleaseDetails
import com.xbot.domain.models.Episode
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.formatters.localizedMessage
import com.xbot.navigation.snackbar.GlobalSnackbarComponent
import com.xbot.navigation.snackbar.show
import com.xbot.resources.Res
import com.xbot.resources.StringResource
import com.xbot.resources.button_retry
import com.xbot.resources.release_details_tab_about
import com.xbot.resources.release_details_tab_episodes
import com.xbot.resources.release_details_tab_ratings
import com.xbot.title.ReleaseTab
import com.xbot.title.TitleScreenAction
import com.xbot.title.TitleScreenSideEffect
import com.xbot.title.TitleScreenState
import com.xbot.title.TitleViewModel
import com.xbot.title.component.PaneSection
import com.xbot.title.component.SectionPaneScaffold
import com.xbot.title.component.playButtonState
import com.xbot.title.component.statusBanner
import com.xbot.title.component.totalDurationMinutes
import com.xbot.title.screen.about.AboutPane
import com.xbot.title.screen.episodes.EpisodesPane
import com.xbot.title.screen.header.HeaderPane
import com.xbot.title.screen.rating.RatingsPane
import io.kotzilla.sdk.compose.TrackScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@TrackScreen
@Composable
internal fun TitleDetailsPane(
    directive: PaneScaffoldDirective,
    onBackClick: () -> Unit,
    onPlayClick: (releaseId: Int, episodeOrdinal: Int) -> Unit,
    onReleaseClick: (Release) -> Unit,
    onGenreClick: (Genre) -> Unit,
    onMemberClick: (ReleaseMember) -> Unit,
    onFranchiseAllClick: () -> Unit,
    onUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is TitleScreenSideEffect.ShowErrorMessage -> {
                GlobalSnackbarComponent.show(sideEffect.error.localizedMessage()) {
                    action(StringResource.Text(Res.string.button_retry)) {
                        sideEffect.onRetry()
                    }
                }
            }
        }
    }

    val onEpisodeClick: (Episode) -> Unit = { episode ->
        state.release?.let { release -> onPlayClick(release.id, episode.ordinal.toInt()) }
    }

    TitleDetailsPaneContent(
        state = state,
        directive = directive,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        onPlayClick = { state.episodeToPlay?.let(onEpisodeClick) },
        onEpisodeClick = onEpisodeClick,
        onReleaseClick = onReleaseClick,
        onGenreClick = onGenreClick,
        onMemberClick = onMemberClick,
        onFranchiseAllClick = onFranchiseAllClick,
        onExternalPlayerClick = {
            state.releaseDetails?.externalPlayerUrl?.let(onUrlClick)
        },
        onUrlClick = onUrlClick,
        modifier = modifier
    )
}

@Composable
private fun TitleDetailsPaneContent(
    state: TitleScreenState,
    directive: PaneScaffoldDirective,
    onAction: (TitleScreenAction) -> Unit,
    onBackClick: () -> Unit,
    onPlayClick: () -> Unit,
    onEpisodeClick: (Episode) -> Unit,
    onReleaseClick: (Release) -> Unit,
    onGenreClick: (Genre) -> Unit,
    onMemberClick: (ReleaseMember) -> Unit,
    onFranchiseAllClick: () -> Unit,
    onExternalPlayerClick: () -> Unit,
    onUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val sections = rememberReleaseSections()

    ProvideShimmer(shimmer) {
        SectionPaneScaffold(
            directive = directive,
            sections = sections,
            selectedSection = state.tab,
            onSectionSelect = { tab -> onAction(TitleScreenAction.OnTabSelect(tab)) },
            modifier = modifier.shimmerUpdater(shimmer),
            topBar = { TitleTopBar(onBackClick = onBackClick) },
            header = {
                HeaderPane(
                    release = state.release,
                    alternativeName = state.releaseDetails?.alternativeName,
                    playButton = state.playButtonState,
                    isBlocked = state.isBlocked,
                    isFavorite = state.isFavorite,
                    collectionStatus = state.collectionStatus,
                    statusBanner = state.statusBanner,
                    onPlayClick = onPlayClick,
                    onFavoriteToggle = { onAction(TitleScreenAction.OnFavoriteToggle) },
                    onCollectionStatusSelect = { status ->
                        onAction(TitleScreenAction.OnCollectionStatusSelect(status))
                    },
                    onExternalPlayerClick = onExternalPlayerClick
                )
            }
        ) { tab ->
            val details = state.releaseDetails
            when (tab) {
                ReleaseTab.Episodes -> EpisodesPane(
                    episodes = state.sortedEpisodes,
                    currentProgress = state.currentProgress,
                    sort = state.episodesSort,
                    onSortChange = { sort ->
                        onAction(TitleScreenAction.OnEpisodesSortChange(sort))
                    },
                    onEpisodeClick = onEpisodeClick
                )

                ReleaseTab.About -> if (details != null) {
                    AboutPane(
                        description = details.release.description,
                        franchiseReleases = state.franchiseReleases.getOrNull().orEmpty(),
                        genres = details.genres,
                        members = details.releaseMembers,
                        totalDurationMinutes = details.release.totalDurationMinutes,
                        lastUpdate = details.freshAt,
                        hasExternalPlayer = details.externalPlayerUrl != null,
                        onFranchiseAllClick = onFranchiseAllClick,
                        onFranchiseReleaseClick = onReleaseClick,
                        onGenreClick = onGenreClick,
                        onMemberClick = onMemberClick,
                        onExternalPlayerClick = onExternalPlayerClick
                    )
                }

                ReleaseTab.Ratings -> if (details != null) {
                    RatingsPane(
                        rating = details.rating,
                        shikimoriRating = details.shikimoriRating,
                        myAnimeListRating = details.myAnimeListRating,
                        collectionCounts = details.collectionCounts,
                        onUrlClick = onUrlClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun rememberReleaseSections(): List<PaneSection<ReleaseTab>> {
    val episodesTitle = stringResource(Res.string.release_details_tab_episodes)
    val aboutTitle = stringResource(Res.string.release_details_tab_about)
    val ratingsTitle = stringResource(Res.string.release_details_tab_ratings)
    return remember(episodesTitle, aboutTitle, ratingsTitle) {
        listOf(
            PaneSection(ReleaseTab.Episodes, SupportingPaneScaffoldRole.Supporting, episodesTitle),
            PaneSection(ReleaseTab.About, SupportingPaneScaffoldRole.Main, aboutTitle),
            PaneSection(ReleaseTab.Ratings, SupportingPaneScaffoldRole.Extra, ratingsTitle)
        )
    }
}

@Composable
private fun TitleTopBar(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Top
        ),
        title = {},
        navigationIcon = {
            FilledIconButton(
                onClick = onBackClick,
                shapes = IconButtonDefaults.shapes()
            ) {
                Icon(
                    imageVector = AnilibertyIcons.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            FilledIconButton(
                onClick = {},
                shapes = IconButtonDefaults.shapes()
            ) {
                Icon(
                    imageVector = AnilibertyIcons.Filled.Star,
                    contentDescription = null
                )
            }
            FilledIconButton(
                onClick = {},
                modifier = Modifier.size(
                    IconButtonDefaults.smallContainerSize(
                        IconButtonDefaults.IconButtonWidthOption.Narrow
                    )
                ),
                shapes = IconButtonDefaults.shapes()
            ) {
                Icon(
                    imageVector = AnilibertyIcons.MoreVert,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
    )
}

@AnilibertyPreview
@Composable
private fun TitleDetailsPanePreview() {
    TitleDetailsPreview(
        directive = PaneScaffoldDirective.Default,
        details = createReleaseDetails(availabilityStatus = AvailabilityStatus.GeoBlocked)
    )
}

@Preview(name = "Two panes", widthDp = 1000, heightDp = 720, locale = "ru")
@Composable
private fun TitleDetailsTwoPanePreview() {
    TitleDetailsPreview(
        directive = PaneScaffoldDirective.Default.copy(
            maxHorizontalPartitions = 2,
            horizontalPartitionSpacerSize = 24.dp
        )
    )
}

@Preview(name = "Three panes", widthDp = 1440, heightDp = 900, locale = "ru")
@Composable
private fun TitleDetailsThreePanePreview() {
    TitleDetailsPreview(
        directive = PaneScaffoldDirective.Default.copy(
            maxHorizontalPartitions = 3,
            horizontalPartitionSpacerSize = 24.dp
        )
    )
}

@Preview(name = "Tabletop", widthDp = 720, heightDp = 840, locale = "ru")
@Composable
private fun TitleDetailsTabletopPreview() {
    TitleDetailsPreview(
        directive = PaneScaffoldDirective.Default.copy(
            maxVerticalPartitions = 2,
            verticalPartitionSpacerSize = 24.dp
        )
    )
}

@Composable
private fun TitleDetailsPreview(
    directive: PaneScaffoldDirective,
    details: ReleaseDetails = createReleaseDetails()
) {
    AnilibertyTheme {
        TitleDetailsPaneContent(
            state = TitleScreenState(
                details = AsyncResult.Success(details),
                franchiseReleases = AsyncResult.Success(ReleaseFixtures.list(count = 3))
            ),
            directive = directive,
            onAction = {},
            onBackClick = {},
            onPlayClick = {},
            onEpisodeClick = {},
            onReleaseClick = {},
            onGenreClick = {},
            onMemberClick = {},
            onFranchiseAllClick = {},
            onExternalPlayerClick = {},
            onUrlClick = {}
        )
    }
}
