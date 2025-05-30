package com.xbot.title

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.AssistChip
import com.xbot.designsystem.components.ChipGroup
import com.xbot.designsystem.components.ExpandableText
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.FeedItemScope
import com.xbot.designsystem.components.FeedScope
import com.xbot.designsystem.components.LabeledIconButton
import com.xbot.designsystem.components.MemberItem
import com.xbot.designsystem.components.NavigableSupportingPaneScaffold
import com.xbot.designsystem.components.ReleaseCardItem
import com.xbot.designsystem.components.ReleaseLargeCard
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.isExpanded
import com.xbot.designsystem.components.row
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.TelegramLogo
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.modifier.verticalParallax
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Episode
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.resources.*
import com.xbot.title.ui.AlertCard
import com.xbot.title.ui.EpisodeListItem
import com.xbot.title.ui.NotificationCard
import com.xbot.shared.ui.feature.title.ui.PlayButton
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TitleScreenContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        onPlayClick = onPlayClick,
        onReleaseClick = onReleaseClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    state: TitleScreenState,
    onAction: (TitleScreenAction) -> Unit,
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val scaffoldNavigator = rememberSupportingPaneScaffoldNavigator<Unit>()
    val scope = rememberCoroutineScope()
    val backBehavior =
        if (scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List) && scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.Detail)) {
            BackNavigationBehavior.PopUntilContentChange
        } else {
            BackNavigationBehavior.PopUntilScaffoldValueChange
        }

    NavigableSupportingPaneScaffold(
        modifier = modifier,
        navigator = scaffoldNavigator,
        defaultBackBehavior = backBehavior,
        mainPane = {
            TitleMainPane(
                state = state,
                onBackClick = onBackClick,
                onPlayClick = onPlayClick,
                onReleaseClick = onReleaseClick,
                onEpisodesListClick = {
                    scope.launch {
                        scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                    }
                }
            )
        },
        supportingPane = {
            TitleSupportingPane(
                state = state,
                onBackClick = {
                    scope.launch {
                        if (scaffoldNavigator.canNavigateBack(backBehavior)) {
                            scaffoldNavigator.navigateBack(backBehavior)
                        }
                    }
                },
                onPlayClick = onPlayClick,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ThreePaneScaffoldPaneScope.TitleMainPane(
    modifier: Modifier = Modifier,
    state: TitleScreenState,
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
    onEpisodesListClick: () -> Unit,
) {
    val gridState = rememberLazyGridState()

    var topAppbarHeight by remember { mutableStateOf(0) }
    val topAppBarAlpha by remember {
        derivedStateOf {
            val firstItem = gridState.layoutInfo.visibleItemsInfo.firstOrNull()
            if (firstItem == null) {
                0f
            } else if (gridState.firstVisibleItemIndex > 0) {
                1f
            } else {
                val scrollProgress = (firstItem.offset.y.absoluteValue / (firstItem.size.height - topAppbarHeight).toFloat()).coerceIn(0f, 1f)
                if (scrollProgress < 0.5f) {
                    0f
                } else {
                    ((scrollProgress - 0.5f) * 2f).coerceIn(0f, 1f)
                }
            }
        }
    }

    AnimatedPane(modifier = modifier) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = topAppBarAlpha)
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) { innerPadding ->
            topAppbarHeight = with(LocalDensity.current) { innerPadding.calculateTopPadding().roundToPx() }

            Crossfade(
                targetState = state,
            ) { targetState ->
                when (targetState) {
                    is TitleScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                    is TitleScreenState.Success -> {
                        TitleDetails(
                            gridState = gridState,
                            details = targetState.title,
                            contentPadding = innerPadding,
                            onPlayClick = onPlayClick,
                            onReleaseClick = onReleaseClick,
                            onEpisodesListClick = onEpisodesListClick,
                            onAddToFavoritesClick = {},
                            onAlreadyWatchedClick = {},
                            onShareClick = {},
                            onTelegramClick = {}
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ThreePaneScaffoldPaneScope.TitleSupportingPane(
    modifier: Modifier = Modifier,
    state: TitleScreenState,
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
) {
    AnimatedPane(modifier = modifier) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = AnilibriaIcons.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Crossfade(
                targetState = state
            ) { targetState ->
                when (targetState) {
                    is TitleScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                    is TitleScreenState.Success -> {
                        EpisodesList(
                            episodes = targetState.title.episodes.reversed(),
                            contentPadding = innerPadding,
                        ) { ordinal ->
                            onPlayClick(targetState.title.release.id, targetState.title.episodes.size - ordinal)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TitleDetails(
    modifier: Modifier = Modifier,
    gridState: LazyGridState,
    details: ReleaseDetail,
    contentPadding: PaddingValues,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
    onEpisodesListClick: () -> Unit,
    onAddToFavoritesClick: () -> Unit,
    onAlreadyWatchedClick: () -> Unit,
    onShareClick: () -> Unit,
    onTelegramClick: () -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    ProvideShimmer(shimmer) {
        Feed(
            modifier = modifier.shimmerUpdater(shimmer),
            state = gridState,
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        ) {
            row {
                ReleaseLargeCard(
                    modifier = Modifier.verticalParallax(gridState),
                    release = details.release
                )
            }

            if (details.availabilityStatus == AvailabilityStatus.Available) {
                swapItems(
                    content1 = {
                        SuggestionRow(
                            onAddToFavoritesClick = onAddToFavoritesClick,
                            onAlreadyWatchedClick = onAlreadyWatchedClick,
                            onShareClick = onShareClick,
                            onTelegramClick = onTelegramClick
                        )
                    },
                    content2 = {
                        PlayButton(
                            modifier = Modifier.fillMaxWidth(),
                            onLeadingClick = { onPlayClick(details.release.id, 0) },
                            onTrailingClick = { onEpisodesListClick() },
                            trailingEnabled = details.episodes.isNotEmpty()
                        )
                    }
                )
            } else {
                row { Spacer(Modifier.height(16.dp)) }

                row {
                    AlertCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        Text(
                            text = when(details.availabilityStatus) {
                                AvailabilityStatus.GeoBlocked -> stringResource(Res.string.alert_blocked_geo)
                                AvailabilityStatus.CopyrightBlocked -> stringResource(Res.string.alert_blocked_copyright)
                                AvailabilityStatus.Available -> ""
                            }
                        )
                    }
                }
            }

            if (details.notification != null) {
                row { Spacer(Modifier.height(16.dp)) }

                row {
                    NotificationCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        Text(text = details.notification!!)
                    }
                }
            }

            if (details.release.description != null) {
                header(
                    title = { Text(text = stringResource(Res.string.label_description)) }
                )
                row {
                    ExpandableText(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = details.release.description!!,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            header(
                title = { Text(text = stringResource(Res.string.label_genres)) }
            )
            row {
                ChipGroup(
                    items = details.genres
                ) { genre ->
                    AssistChip(
                        onClick = {},
                        label = { Text(text = genre.name) }
                    )
                }
            }

            if (details.releaseMembers.isNotEmpty()) {
                header(
                    title = { Text(text = stringResource(Res.string.label_members)) }
                )
                horizontalItems(
                    items = details.releaseMembers,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) { member ->
                    MemberItem(
                        releaseMember = member,
                        onClick = { /*TODO*/ }
                    )
                }
            }

            if (details.relatedReleases.isNotEmpty()) {
                header(
                    title = { Text(text = stringResource(Res.string.label_related_releases)) }
                )
                horizontalItems(
                    items = details.relatedReleases,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) { release ->
                    ReleaseCardItem(
                        release = release,
                        onClick = { onReleaseClick(it.id) }
                    )
                }
            }

            row { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun EpisodesList(
    episodes: List<Episode>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onEpisodeClick: (Int) -> Unit
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    ProvideShimmer(shimmer) {
        LazyColumn(
            modifier = modifier.shimmerUpdater(shimmer),
            contentPadding = contentPadding,
            reverseLayout = false
        ) {
            itemsIndexed(episodes) { index, episode ->
                EpisodeListItem(episode) {
                    onEpisodeClick(index + 1)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SuggestionRow(
    modifier: Modifier = Modifier,
    onAddToFavoritesClick: () -> Unit,
    onAlreadyWatchedClick: () -> Unit,
    onShareClick: () -> Unit,
    onTelegramClick: () -> Unit,
) {
    ButtonGroup(
        modifier = modifier,
    ) {
        LabeledIconButton(
            modifier = Modifier
                .weight(1f),
            onClick = onAddToFavoritesClick,
            text = stringResource(Res.string.button_add_to_favorites),
            icon = AnilibriaIcons.Filled.Star,
        )
        LabeledIconButton(
            modifier = Modifier
                .weight(1f),
            onClick = onAlreadyWatchedClick,
            text = stringResource(Res.string.button_watched_it),
            icon = AnilibriaIcons.Outlined.CheckList,
        )
        LabeledIconButton(
            modifier = Modifier
                .weight(1f),
            onClick = onShareClick,
            text = stringResource(Res.string.button_share),
            icon = AnilibriaIcons.Filled.Share,
        )
        LabeledIconButton(
            modifier = Modifier
                .weight(1f),
            onClick = onTelegramClick,
            text = stringResource(Res.string.button_telegram),
            icon = AnilibriaIcons.Filled.TelegramLogo,
        )
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    ProvideShimmer(shimmer) {
        Column(
            modifier = modifier
                .padding(contentPadding.only(WindowInsetsSides.Horizontal))
                .verticalScroll(rememberScrollState(), enabled = false)
        ) {
            ReleaseLargeCard(null)
        }
    }
}

private fun FeedScope.swapItems(
    span: (LazyGridItemSpanScope.() -> GridItemSpan)? = { GridItemSpan(1) },
    content1: @Composable FeedItemScope.() -> Unit,
    content2: @Composable FeedItemScope.() -> Unit,
) {
    item(span = span) {
        Box(
            modifier = Modifier
                .feedItemSpacing(0)
                .padding(bottom = if (maxLineSpan == 1) 16.dp else 0.dp),
            contentAlignment = Alignment.Center
        ) {
            if (maxLineSpan == 1) {
                content1()
            } else {
                content2()
            }
        }
    }
    item(span = span) {
        Box(
            modifier = Modifier
                .feedItemSpacing(1),
            contentAlignment = Alignment.Center
        ) {
            if (maxLineSpan == 1) {
                content2()
            } else {
                content1()
            }
        }
    }
}
