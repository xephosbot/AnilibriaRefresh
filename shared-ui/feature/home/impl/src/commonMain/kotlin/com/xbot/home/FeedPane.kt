package com.xbot.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ConnectedButtonGroupDefaults
import com.xbot.designsystem.components.EpisodeListItem
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.GenreItem
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.LargeReleaseCard
import com.xbot.designsystem.components.MediumReleaseCard
import com.xbot.designsystem.components.MediumSplitButton
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
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.AnilibriaLogoLarge
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.fadeWithParallax
import com.xbot.designsystem.modifier.horizontalParallax
import com.xbot.designsystem.modifier.overlayDrawable
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.modifier.verticalParallax
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Release
import com.xbot.resources.Res
import com.xbot.resources.badge_1
import com.xbot.resources.badge_2
import com.xbot.resources.badge_3
import com.xbot.resources.button_watch
import com.xbot.resources.label_best
import com.xbot.resources.label_best_all_time
import com.xbot.resources.label_best_now
import com.xbot.resources.label_genres
import com.xbot.resources.label_schedule_now
import com.xbot.resources.label_updates
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.absoluteValue

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
context(scope: ThreePaneScaffoldPaneScope)
internal fun FeedPane(
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = koinViewModel(),
    onSearchClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val items = viewModel.releases.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    FeedPane(
        modifier = modifier,
        state = state,
        items = items,
        onAction = viewModel::onAction,
        onSearchClick = onSearchClick,
        onScheduleClick = onScheduleClick,
        onReleaseClick = onReleaseClick,
    )
}

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
context(scope: ThreePaneScaffoldPaneScope)
private fun FeedPane(
    modifier: Modifier = Modifier,
    state: FeedScreenState,
    items: LazyPagingItems<Release>,
    onAction: (FeedScreenAction) -> Unit,
    onSearchClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    var topAppbarHeight by remember { mutableStateOf(0) }
    val topAppBarAlpha by remember {
        derivedStateOf {
            val firstItem = gridState.layoutInfo.visibleItemsInfo.firstOrNull()
            if (firstItem == null) {
                0f
            } else if (gridState.firstVisibleItemIndex > 0) {
                1f
            } else {
                val scrollProgress =
                    (firstItem.offset.y.absoluteValue / (firstItem.size.height - topAppbarHeight).toFloat()).coerceIn(
                        0f,
                        1f
                    )
                if (scrollProgress < 0.5f) {
                    0f
                } else {
                    ((scrollProgress - 0.5f) * 2f).coerceIn(0f, 1f)
                }
            }
        }
    }

    val onShowErrorMessage: (Throwable) -> Unit = { error ->
        onAction(FeedScreenAction.ShowErrorMessage(error) { items.retry() })
    }

    LaunchedEffect(items) {
        snapshotFlow { items.loadState }.collect { loadState ->
            (loadState.refresh as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
            (loadState.append as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
            (loadState.prepend as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
        }
    }

    val isRefreshing by remember(state, items) {
        derivedStateOf { items.loadState.refresh == LoadState.Loading || state is FeedScreenState.Loading }
    }

    val showResetScrollButton by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0
        }
    }

    with(scope) {
        AnimatedPane(modifier = modifier) {
            Scaffold(
                modifier = Modifier.pullToRefresh(
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState,
                    onRefresh = {
                        items.refresh()
                        onAction(FeedScreenAction.Refresh)
                    }
                ),
                topBar = {
                    TopAppBar(
                        title = {
                            Image(
                                imageVector = AnilibriaIcons.Filled.AnilibriaLogoLarge,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                contentDescription = null
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = onSearchClick,
                                shapes = IconButtonDefaults.shapes()
                            ) {
                                Icon(
                                    imageVector = AnilibriaIcons.Outlined.Search,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = topAppBarAlpha),
                        )
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = gridState.isScrollingUp().value && showResetScrollButton,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        FilledTonalIconButton(
                            onClick = {
                                coroutineScope.launch {
                                    gridState.animateScrollToItem(0)
                                }
                            },
                            shapes = IconButtonDefaults.shapes()
                        ) {
                            Icon(
                                modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
                                imageVector = AnilibriaIcons.Outlined.ArrowDropUp,
                                contentDescription = null
                            )
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
                containerColor = MaterialTheme.colorScheme.surface
            ) { innerPadding ->
                topAppbarHeight =
                    with(LocalDensity.current) { innerPadding.calculateTopPadding().roundToPx() }

                Box {
                    when (state) {
                        is FeedScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                        is FeedScreenState.Success -> {
                            ReleaseFeed(
                                gridState = gridState,
                                items = items,
                                state = state,
                                contentPadding = innerPadding,
                                onScheduleClick = onScheduleClick,
                                onBestTypeChange = { onAction(FeedScreenAction.UpdateBestType(it)) },
                                onReleaseClick = { onReleaseClick(it.id) },
                            )
                        }
                    }

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
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ReleaseFeed(
    modifier: Modifier = Modifier,
    gridState: LazyGridState,
    items: LazyPagingItems<Release>,
    state: FeedScreenState.Success,
    contentPadding: PaddingValues,
    onScheduleClick: () -> Unit,
    onBestTypeChange: (BestType) -> Unit,
    onReleaseClick: (Release) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val pagerState = rememberPagerState(pageCount = { state.recommendedReleases.size })

    ProvideShimmer(shimmer) {
        Feed(
            modifier = modifier.shimmerUpdater(shimmer),
            state = gridState,
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        ) {
            horizontalPagerItems(
                items = state.recommendedReleases,
                state = pagerState
            ) { page, release ->
                LargeReleaseCard(
                    modifier = Modifier
                        .horizontalParallax(pagerState, page)
                        .verticalParallax(gridState),
                    contentModifier = Modifier
                        .fadeWithParallax(pagerState, page),
                    release = release
                ) {
                    var checked by remember { mutableStateOf(false) }

                    MediumSplitButton(
                        onLeadingClick = {
                            onReleaseClick(release)
                        },
                        trailingChecked = checked,
                        onTrailingCheckedChange = { checked = it },
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
                            val rotation by animateFloatAsState(if (checked) 180f else 0f)

                            Icon(
                                modifier = Modifier
                                    .size(SplitButtonDefaults.trailingButtonIconSizeFor(SplitButtonDefaults.MediumContainerHeight))
                                    .graphicsLayer {
                                        rotationZ = rotation
                                    },
                                imageVector = AnilibriaIcons.Outlined.ArrowDropDown,
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

            header(
                title = { Text(text = stringResource(Res.string.label_schedule_now)) },
                onClick = onScheduleClick
            )
            horizontalSnappableItems(
                items = state.scheduleNow,
                key = { it.release.id },
                contentPadding = PaddingValues(horizontal = 16.dp),
                itemSpacing = 16.dp,
            ) { schedule ->
                MediumReleaseCard(
                    modifier = Modifier,
                    release = schedule.release,
                    onClick = onReleaseClick,
                ) {
                    EpisodeListItem(
                        episode = schedule.publishedReleaseEpisode,
                        onClick = {}
                    )
                }
            }

            row {
                Header(
                    title = { Text(text = stringResource(Res.string.label_best)) },
                    content = {
                        val items = remember { BestType.entries }
                        SingleChoiceConnectedButtonGroup(
                            items = items,
                            selectedItem = state.currentBestType
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
                items = state.bestReleases,
                key = { index, item -> item.id },
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) { index, release ->
                AnilibriaTheme(darkTheme = false) {
                    SmallReleaseCard(
                        modifier = Modifier
                            .then(
                                when (index) {
                                    0 -> Modifier.overlayDrawable(
                                        resource = Res.drawable.badge_1,
                                        offset = DpOffset(x = 70.dp, y = 11.dp)
                                    )

                                    1 -> Modifier.overlayDrawable(
                                        resource = Res.drawable.badge_2,
                                        offset = DpOffset(x = 70.dp, y = 11.dp)
                                    )

                                    2 -> Modifier.overlayDrawable(
                                        resource = Res.drawable.badge_3,
                                        offset = DpOffset(x = 70.dp, y = 11.dp)
                                    )

                                    else -> Modifier
                                }
                            ),
                        release = release,
                        onClick = onReleaseClick,
                    )
                }
            }

            header(
                title = { Text(text = stringResource(Res.string.label_genres)) }
            )
            horizontalItems(
                items = state.genres,
                key = { it.id },
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) { genre ->
                GenreItem(
                    genre = genre,
                    onClick = { /*TODO*/ }
                )
            }

            header(
                title = { Text(text = stringResource(Res.string.label_updates)) },
            )
            pagingItems(items) { index, release ->
                Column {
                    ReleaseListItem(
                        modifier = Modifier.feedItemSpacing(index),
                        release = release,
                        onClick = onReleaseClick,
                    )
                    if (index < items.itemCount - 1) {
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    ProvideShimmer(shimmer) {
        Column(
            modifier = modifier
                .padding(contentPadding.only(WindowInsetsSides.Horizontal))
                .verticalScroll(rememberScrollState(), enabled = false)
        ) {
            LargeReleaseCard(null)
            Header(
                title = { Text(stringResource(Res.string.label_schedule_now)) },
                onClick = {},
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState(), enabled = false)
                    .padding(horizontal = 16.dp)
            ) {
                repeat(10) {
                    MediumReleaseCard(
                        release = null,
                        onClick = {}
                    )
                }
            }
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