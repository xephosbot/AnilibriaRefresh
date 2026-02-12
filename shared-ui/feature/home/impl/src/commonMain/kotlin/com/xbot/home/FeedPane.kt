package com.xbot.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TonalToggleButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ConnectedButtonGroupDefaults
import com.xbot.designsystem.components.EpisodeListItem
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.FranchiseCard
import com.xbot.designsystem.components.GenreItem
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.LargeReleaseCard
import com.xbot.designsystem.components.MediumReleaseCard
import com.xbot.designsystem.components.MediumSplitButton
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.SingleChoiceConnectedButtonGroup
import com.xbot.designsystem.components.SmallReleaseCard
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.horizontalItemsIndexed
import com.xbot.designsystem.components.horizontalPagerItems
import com.xbot.designsystem.components.horizontalSnappableItems
import com.xbot.designsystem.components.pagingItems
import com.xbot.designsystem.components.row
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.AnilibriaLogoLarge
import com.xbot.designsystem.icons.ArrowDropDown
import com.xbot.designsystem.icons.ArrowDropUp
import com.xbot.designsystem.icons.PlayArrow
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.fadeWithParallax
import com.xbot.designsystem.modifier.horizontalParallax
import com.xbot.designsystem.modifier.overlayDrawable
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.modifier.verticalParallax
import com.xbot.designsystem.theme.LocalMargins
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleasesFeed
import com.xbot.fixtures.data.franchiseMocks
import com.xbot.fixtures.data.genreMocks
import com.xbot.fixtures.data.releaseMocks
import com.xbot.fixtures.data.scheduleMocks
import com.xbot.resources.Res
import com.xbot.resources.badge_1
import com.xbot.resources.badge_2
import com.xbot.resources.badge_3
import com.xbot.resources.button_watch
import com.xbot.resources.label_best
import com.xbot.resources.label_best_all_time
import com.xbot.resources.label_best_now
import com.xbot.resources.label_franchises
import com.xbot.resources.label_genres
import com.xbot.resources.label_schedule_now
import com.xbot.resources.label_updates
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
internal fun FeedPane(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onScheduleClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
    onEpisodeClick: (Int, Int) -> Unit,
    onProfileClick: () -> Unit,
) {
    val items = viewModel.releases.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    FeedPaneContent(
        modifier = modifier,
        state = state,
        items = items,
        onAction = viewModel::onAction,
        onScheduleClick = onScheduleClick,
        onReleaseClick = onReleaseClick,
        onEpisodeClick = onEpisodeClick,
        onProfileClick = onProfileClick
    )
}

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
private fun FeedPaneContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    items: LazyPagingItems<Release>,
    onAction: (HomeScreenAction) -> Unit,
    onScheduleClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
    onEpisodeClick: (Int, Int) -> Unit,
    onProfileClick: () -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    val onShowErrorMessage: (Throwable) -> Unit = { error ->
        onAction(HomeScreenAction.ShowErrorMessage(error) { items.retry() })
    }

    LaunchedEffect(items) {
        snapshotFlow { items.loadState }.collect { loadState ->
            (loadState.refresh as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
            (loadState.append as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
            (loadState.prepend as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
        }
    }

    val isRefreshing by remember(state, items) {
        derivedStateOf { items.loadState.refresh == LoadState.Loading }
    }

    val showResetScrollButton by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0
        }
    }

    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    ProvideShimmer(shimmer) {
        Scaffold(
            modifier = modifier
                .pullToRefresh(
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState,
                    onRefresh = {
                        items.refresh()
                        onAction(HomeScreenAction.Refresh)
                    }
                )
                .shimmerUpdater(shimmer),
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surfaceContainer,
                                    MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0f),
                                )
                            )
                        ),
                    title = {
                        Image(
                            imageVector = AnilibriaIcons.AnilibriaLogoLarge,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            contentDescription = null
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = onProfileClick,
                            shapes = IconButtonDefaults.shapes()
                        ) {
                            PosterImage(
                                modifier = Modifier
                                    .size(IconButtonDefaults.smallContainerSize()),
                                poster = state.currentUser?.avatar
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
                )
            },
            floatingActionButton = {
                FilledTonalIconButton(
                    modifier = Modifier.animateFloatingActionButton(
                        visible = gridState.isScrollingUp().value && showResetScrollButton,
                        alignment = Alignment.Center
                    ),
                    onClick = {
                        coroutineScope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    },
                    shapes = IconButtonDefaults.shapes()
                ) {
                    Icon(
                        modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
                        imageVector = AnilibriaIcons.ArrowDropUp,
                        contentDescription = null
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ) { innerPadding ->
            Box {
                ReleaseFeed(
                    gridState = gridState,
                    items = items,
                    releasesFeed = state.releasesFeed,
                    currentBestType = state.currentBestType,
                    contentPadding = innerPadding,
                    onScheduleClick = onScheduleClick,
                    onBestTypeChange = { onAction(HomeScreenAction.UpdateBestType(it)) },
                    onReleaseClick = { onReleaseClick(it.id) },
                    onEpisodeClick = onEpisodeClick,
                )

                LoadingIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(innerPadding.only(WindowInsetsSides.Top)),
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalTime::class)
@Composable
private fun ReleaseFeed(
    modifier: Modifier = Modifier,
    gridState: LazyGridState,
    items: LazyPagingItems<Release>,
    releasesFeed: ReleasesFeed,
    currentBestType: BestType,
    contentPadding: PaddingValues,
    onScheduleClick: () -> Unit,
    onBestTypeChange: (BestType) -> Unit,
    onReleaseClick: (Release) -> Unit,
    onEpisodeClick: (Int, Int) -> Unit,
) {
    val pagerState = rememberPagerState { releasesFeed.recommendedReleases.size }
    val columnsCount = remember {
        derivedStateOf { gridState.layoutInfo.maxSpan }
    }
    
    var activeMenuReleaseId by remember { mutableStateOf<Int?>(null) }
    val horizontalMargin = LocalMargins.current.horizontal

    Feed(
        modifier = modifier,
        state = gridState,
        columns = GridCells.Adaptive(400.dp),
        contentPadding = contentPadding.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
    ) {
        horizontalPagerItems(
            items = releasesFeed.recommendedReleases,
            state = pagerState,
            isAutoScrollActive = activeMenuReleaseId == null,
        ) { page, release ->
            LargeReleaseCard(
                modifier = Modifier
                    .horizontalParallax(pagerState, page)
                    .verticalParallax(gridState),
                contentModifier = Modifier
                    .fadeWithParallax(pagerState, page),
                release = release
            ) {
                release?.let {
                    val isChecked by rememberUpdatedState(activeMenuReleaseId == release.id)

                    MediumSplitButton(
                        onLeadingClick = {
                            onReleaseClick(release)
                        },
                        trailingChecked = isChecked,
                        onTrailingCheckedChange = { checked ->
                            activeMenuReleaseId = if (checked) release.id else null
                        },
                        leadingContent = {
                            Icon(
                                modifier = Modifier
                                    .size(SplitButtonDefaults.leadingButtonIconSizeFor(SplitButtonDefaults.MediumContainerHeight)),
                                imageVector = AnilibriaIcons.Filled.PlayArrow,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(ButtonDefaults.MediumIconSpacing))
                            Text(
                                text = stringResource(Res.string.button_watch),
                                maxLines = 1
                            )
                        },
                        trailingContent = {
                            val rotation by animateFloatAsState(if (isChecked) 180f else 0f)

                            Icon(
                                modifier = Modifier
                                    .size(SplitButtonDefaults.trailingButtonIconSizeFor(SplitButtonDefaults.MediumContainerHeight))
                                    .graphicsLayer {
                                        rotationZ = rotation
                                    },
                                imageVector = AnilibriaIcons.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Item 1") },
                            onClick = { /* Handle item 1 click */ }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Item 2") },
                            onClick = { /* Handle item 3 click */ }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Item 3") },
                            onClick = { /* Handle item 3 click */ }
                        )
                    }
                }
            }
        }

        header(
            title = { Text(text = stringResource(Res.string.label_schedule_now)) },
            onClick = onScheduleClick
        )
        horizontalSnappableItems(
            items = releasesFeed.scheduleNow,
            contentPadding = PaddingValues(horizontal = horizontalMargin),
            itemSpacing = 16.dp,
        ) { schedule ->
            MediumReleaseCard(
                modifier = Modifier,
                release = schedule?.release,
                onClick = onReleaseClick,
            ) {
                schedule?.let {
                    val episode = schedule.toEpisode()

                    EpisodeListItem(
                        episode = episode,
                        onClick = {
                            onEpisodeClick(schedule.release.id, episode.ordinal.toInt())
                        }
                    )
                }
            }
        }

        row {
            Header(
                title = { Text(text = stringResource(Res.string.label_best)) },
                content = {
                    val items = remember { BestType.entries }
                    SingleChoiceConnectedButtonGroup(
                        items = items,
                        selectedItem = currentBestType
                    ) { selected, item ->
                        TonalToggleButton(
                            modifier = Modifier.height(ButtonDefaults.ExtraSmallContainerHeight),
                            checked = selected,
                            onCheckedChange = { onBestTypeChange(item) },
                            shapes = ConnectedButtonGroupDefaults.connectedButtonShapes(
                                index = items.indexOf(item),
                                count = items.size
                            ),
                            contentPadding = ButtonDefaults.ExtraSmallContentPadding,
                        ) {
                            Text(text = stringResource(item.stringRes))
                        }
                    }
                }
            )
        }
        horizontalItemsIndexed(
            items = if (currentBestType == BestType.Now) releasesFeed.bestNow else releasesFeed.bestAllTime,
            contentPadding = PaddingValues(horizontal = horizontalMargin),
        ) { index, release ->
            // TODO: SmallReleaseCard might need to support null release
            release?.let {
                SmallReleaseCard(
                    modifier = Modifier.badgeOverlay(
                        index = index,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                MaterialTheme.colorScheme.primaryFixedDim
                            ),
                            startY = 0.0f,
                            endY = 250.0f
                        )
                    ),
                    release = release,
                    onClick = onReleaseClick,
                )
            }
        }

        header(
            title = { Text(text = stringResource(Res.string.label_franchises)) },
        )
        horizontalSnappableItems(
            items = releasesFeed.recommendedFranchises,
            contentPadding = PaddingValues(horizontal = horizontalMargin),
            itemSpacing = 16.dp,
        ) { franchise ->
            // TODO: FranchiseCard might need to support null franchise
            franchise?.let {
                FranchiseCard(
                    franchise = franchise,
                    onClick = { /*TODO*/ }
                )
            }
        }

        header(
            title = { Text(text = stringResource(Res.string.label_genres)) }
        )
        horizontalItems(
            items = releasesFeed.genres,
            contentPadding = PaddingValues(horizontal = horizontalMargin),
        ) { genre ->
             // TODO: GenreItem might need to support null genre
            genre?.let {
                GenreItem(
                    genre = genre,
                    onClick = { /*TODO*/ }
                )
            }
        }

        header(
            title = { Text(text = stringResource(Res.string.label_updates)) },
        )
        pagingItems(items) { index, release ->
            ReleaseListItem(
                modifier = Modifier
                    .section(index, items.itemCount, columnsCount.value),
                release = release,
                onClick = onReleaseClick,
            )
        }
    }
}

@Composable
private fun LazyGridState.isScrollingUp(): State<Boolean> {
    return produceState(initialValue = true) {
        var lastIndex = 0
        var lastScroll = Int.MAX_VALUE
        snapshotFlow {
            firstVisibleItemIndex to firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentScroll) ->
            if (currentIndex != lastIndex || currentScroll != lastScroll) {
                value = currentIndex < lastIndex ||
                        (currentIndex == lastIndex && currentScroll < lastScroll)
                lastIndex = currentIndex
                lastScroll = currentScroll
            }
        }
    }
}

private val BestType.stringRes: StringResource
    get() = when (this) {
        BestType.Now -> Res.string.label_best_now
        BestType.AllTime -> Res.string.label_best_all_time
    }

private val Int.badgeDrawableRes: DrawableResource
    get() = when (this) {
        0 -> Res.drawable.badge_1
        1 -> Res.drawable.badge_2
        2 -> Res.drawable.badge_3
        else -> throw IllegalStateException("Invalid badge index: $this")
    }

@Composable
private fun Modifier.badgeOverlay(index: Int, brush: Brush): Modifier {
    return if (index < 3) {
        this.overlayDrawable(
            resource = index.badgeDrawableRes,
            brush = brush,
            offset = DpOffset(x = 70.dp, y = 11.dp)
        )
    } else {
        this
    }
}

@Preview
@Composable
private fun FeedPanePreview(
    @PreviewParameter(FeedScreenStateProvider::class) state: HomeScreenState
) {
    AnilibriaPreview {
        val items = flowOf(PagingData.from(releaseMocks)).collectAsLazyPagingItems()

        FeedPaneContent(
            state = state,
            items = items,
            onAction = {},
            onScheduleClick = {},
            onReleaseClick = {},
            onEpisodeClick = { _, _ -> },
            onProfileClick = {}
        )
    }
}

private class FeedScreenStateProvider : PreviewParameterProvider<HomeScreenState> {
    override val values = sequenceOf(
        HomeScreenState(
            releasesFeed = ReleasesFeed()
        ),
        HomeScreenState(
            releasesFeed = ReleasesFeed(
                recommendedReleases = releaseMocks,
                scheduleNow = scheduleMocks,
                bestNow = releaseMocks,
                bestAllTime = releaseMocks,
                recommendedFranchises = franchiseMocks,
                genres = genreMocks
            )
        )
    )
}
