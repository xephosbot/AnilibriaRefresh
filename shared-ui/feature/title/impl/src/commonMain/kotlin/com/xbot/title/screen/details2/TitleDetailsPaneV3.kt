package com.xbot.title.screen.details2

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.common.getOrNull
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.MoreVert
import com.xbot.designsystem.icons.Star
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.theme.AnilibertyTheme
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.title.component.SectionPaneScaffold
import com.xbot.title.component.totalDurationMinutes
import com.xbot.title.screen.about.AboutPane
import com.xbot.title.screen.episodes.EpisodesPane
import com.xbot.title.screen.header.HeaderPane
import com.xbot.title.screen.rating.RatingsPane

@Composable
internal fun TitleDetailsPaneContentV3(
    state: TitleDetailsStateV2,
    directive: PaneScaffoldDirective,
    modifier: Modifier = Modifier,
    onAction: (TitleDetailsActionV2) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val sections = rememberReleaseSections()

    ProvideShimmer(shimmer) {
        SectionPaneScaffold(
            directive = directive,
            sections = sections,
            selectedSection = state.tab.toSection(),
            onSectionSelect = { section ->
                section.toTab()?.let { tab -> onAction(TitleDetailsActionV2.OnTabSelect(tab)) }
            },
            modifier = modifier.shimmerUpdater(shimmer),
            topBar = { TitleTopBar(onBackClick = onBackClick) }
        ) { section ->
            val details = state.releaseDetails
            when (section) {
                ReleaseSection.Header -> HeaderPane(
                    release = state.release,
                    alternativeName = details?.alternativeName,
                    playButton = state.playButton,
                    isBlocked = state.isBlocked,
                    isFavorite = state.isFavorite,
                    collectionStatus = state.collectionStatus,
                    statusBanner = state.statusBanner,
                    onPlayClick = { onAction(TitleDetailsActionV2.OnPlayClick) },
                    onFavoriteToggle = { onAction(TitleDetailsActionV2.OnFavoriteToggle) },
                    onCollectionStatusSelect = { status ->
                        onAction(TitleDetailsActionV2.OnCollectionStatusSelect(status))
                    },
                    onExternalPlayerClick = {
                        onAction(TitleDetailsActionV2.OnExternalPlayerClick)
                    }
                )

                ReleaseSection.Episodes -> EpisodesPane(
                    episodes = state.sortedEpisodes,
                    episodesProgress = state.episodesProgress,
                    currentProgress = state.currentProgress,
                    sort = state.episodesSort,
                    onSortChange = { sort ->
                        onAction(TitleDetailsActionV2.OnEpisodesSortChange(sort))
                    },
                    onEpisodeClick = { episode ->
                        onAction(TitleDetailsActionV2.OnEpisodeClick(episode))
                    }
                )

                ReleaseSection.About -> if (details != null) {
                    AboutPane(
                        description = details.release.description,
                        franchiseReleases = state.franchiseReleases.getOrNull().orEmpty(),
                        genres = details.genres,
                        members = details.releaseMembers,
                        totalDurationMinutes = details.release.totalDurationMinutes,
                        lastUpdate = details.freshAt,
                        hasExternalPlayer = details.externalPlayerUrl != null,
                        onFranchiseAllClick = {
                            onAction(TitleDetailsActionV2.OnFranchiseAllClick)
                        },
                        onFranchiseReleaseClick = { release ->
                            onAction(TitleDetailsActionV2.OnFranchiseReleaseClick(release))
                        },
                        onGenreClick = { genre ->
                            onAction(TitleDetailsActionV2.OnGenreClick(genre))
                        },
                        onMemberClick = { member ->
                            onAction(TitleDetailsActionV2.OnMemberClick(member))
                        },
                        onExternalPlayerClick = {
                            onAction(TitleDetailsActionV2.OnExternalPlayerClick)
                        }
                    )
                }

                ReleaseSection.Ratings -> if (details != null) {
                    RatingsPane(
                        rating = details.rating,
                        shikimoriRating = details.shikimoriRating,
                        myAnimeListRating = details.myAnimeListRating,
                        collectionCounts = details.collectionCounts,
                        onUrlClick = { url -> onAction(TitleDetailsActionV2.OnUrlClick(url)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleTopBar(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
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
    TitleDetailsPaneContentV3(
        state = TitleDetailsPreviewDataV2.geoBlocked,
        directive = PaneScaffoldDirective.Default
    )
}

@Preview(name = "Two panes", widthDp = 1000, heightDp = 720, locale = "ru")
@Composable
private fun TitleDetailsTwoPanePreview() {
    TitleDetailsAdaptivePreview(
        directive = PaneScaffoldDirective.Default.copy(
            maxHorizontalPartitions = 2,
            horizontalPartitionSpacerSize = 24.dp
        )
    )
}

@Preview(name = "Three panes", widthDp = 1440, heightDp = 900, locale = "ru")
@Composable
private fun TitleDetailsThreePanePreview() {
    TitleDetailsAdaptivePreview(
        directive = PaneScaffoldDirective.Default.copy(
            maxHorizontalPartitions = 3,
            horizontalPartitionSpacerSize = 24.dp
        )
    )
}

@Preview(name = "Tabletop", widthDp = 720, heightDp = 840, locale = "ru")
@Composable
private fun TitleDetailsTabletopPreview() {
    TitleDetailsAdaptivePreview(
        directive = PaneScaffoldDirective.Default.copy(
            maxVerticalPartitions = 2,
            verticalPartitionSpacerSize = 24.dp
        )
    )
}

@Composable
private fun TitleDetailsAdaptivePreview(directive: PaneScaffoldDirective) {
    AnilibertyTheme {
        TitleDetailsPaneContentV3(
            state = TitleDetailsPreviewDataV2.ongoing,
            directive = directive
        )
    }
}
