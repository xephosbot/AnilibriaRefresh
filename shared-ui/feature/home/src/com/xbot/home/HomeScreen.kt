package com.xbot.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ExpressiveButton
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.FilledIconButton
import com.xbot.designsystem.components.GenreItem
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.NavigableSupportingPaneScaffold
import com.xbot.designsystem.components.ReleaseCardItem
import com.xbot.designsystem.components.ReleaseLargeCard
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.horizontalPagerItems
import com.xbot.designsystem.components.isExpanded
import com.xbot.designsystem.components.pagingItems
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.AnilibriaLogoLarge
import com.xbot.designsystem.modifier.*
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.localization.toLocalizedString
import com.xbot.resources.*
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onSearchClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val items = viewModel.releases.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        modifier = modifier,
        state = state,
        items = items,
        onAction = viewModel::onAction,
        onSearchClick = onSearchClick,
        onReleaseClick = onReleaseClick,
    )
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    items: LazyPagingItems<Release>,
    onAction: (HomeScreenAction) -> Unit,
    onSearchClick: () -> Unit,
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
            HomeMainPane(
                state = state,
                items = items,
                onSearchClick = onSearchClick,
                onScheduleClick = {
                    scope.launch {
                        scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                    }
                },
                onReleaseClick = onReleaseClick,
                onRefresh = {
                    items.refresh()
                    onAction(HomeScreenAction.Refresh)
                },
                onShowErrorMessage = { error ->
                    onAction(HomeScreenAction.ShowErrorMessage(error) { items.retry() })
                }
            )
        },
        supportingPane = {
            HomeSupportingPane(
                onReleaseClick = onReleaseClick,
                onBackClick = {
                    scope.launch {
                        if (scaffoldNavigator.canNavigateBack(backBehavior)) {
                            scaffoldNavigator.navigateBack(backBehavior)
                        }
                    }
                }
            )
        }
    )
}

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
private fun ThreePaneScaffoldPaneScope.HomeMainPane(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    items: LazyPagingItems<Release>,
    onSearchClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    onShowErrorMessage: (Throwable) -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
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

    LaunchedEffect(items) {
        snapshotFlow { items.loadState }.collect { loadState ->
            (loadState.refresh as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
            (loadState.append as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
            (loadState.prepend as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
        }
    }

    val isRefreshing by remember(state, items) {
        derivedStateOf { items.loadState.refresh == LoadState.Loading || state is HomeScreenState.Loading }
    }

    AnimatedPane(modifier = modifier) {
        Scaffold(
            modifier = Modifier.pullToRefresh(
                isRefreshing = isRefreshing,
                state = pullToRefreshState,
                onRefresh = onRefresh
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
                            onClick = onSearchClick
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
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) { innerPadding ->
            topAppbarHeight = with(LocalDensity.current) { innerPadding.calculateTopPadding().roundToPx() }

            Box {
                Crossfade(
                    targetState = state,
                    label = "Loading state transition in HomeScreenContent",
                ) { targetState ->
                    when (targetState) {
                        is HomeScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                        is HomeScreenState.Success -> {
                            ReleaseFeed(
                                gridState = gridState,
                                items = items,
                                recommendedReleases = targetState.releasesFeed.recommendedReleases,
                                genres = targetState.releasesFeed.genres,
                                scheduleList = targetState.releasesFeed.schedule,
                                contentPadding = innerPadding,
                                onScheduleClick = onScheduleClick,
                                onReleaseClick = { onReleaseClick(it.id) },
                            )
                        }
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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ThreePaneScaffoldPaneScope.HomeSupportingPane(
    modifier: Modifier = Modifier,
    onReleaseClick: (Int) -> Unit,
    onBackClick: () -> Unit,
) {
    AnimatedPane(modifier = modifier) {
        ScheduleScreen(
            onReleaseClick = onReleaseClick,
            onBackClick = onBackClick
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ReleaseFeed(
    modifier: Modifier = Modifier,
    gridState: LazyGridState,
    items: LazyPagingItems<Release>,
    recommendedReleases: List<Release>,
    genres: List<Genre>,
    scheduleList: Map<DayOfWeek, List<Release>>,
    contentPadding: PaddingValues,
    onScheduleClick: () -> Unit,
    onReleaseClick: (Release) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val pagerState = rememberPagerState(pageCount = { recommendedReleases.size })

    ProvideShimmer(shimmer) {
        Feed(
            modifier = modifier.shimmerUpdater(shimmer),
            state = gridState,
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        ) {
            horizontalPagerItems(
                items = recommendedReleases,
                state = pagerState
            ) { page, release ->
                ReleaseLargeCard(
                    modifier = Modifier
                        .horizontalParallax(pagerState, page)
                        .verticalParallax(gridState),
                    contentModifier = Modifier
                        .fadeWithParallax(pagerState, page),
                    release = release
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ExpressiveButton(
                            onClick = { onReleaseClick(release) },
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                        ) {
                            Icon(
                                modifier = Modifier.size(ButtonDefaults.MediumIconSize),
                                imageVector = AnilibriaIcons.Outlined.PlayArrow,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                            Text(text = stringResource(Res.string.button_watch))
                        }
                        Spacer(Modifier.width(8.dp))
                        FilledIconButton(
                            onClick = {},
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceBright,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(
                                modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
                                imageVector = AnilibriaIcons.Outlined.Star,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            header(
                title = { Text(text = stringResource(Res.string.label_schedule)) },
                onClick = onScheduleClick
            )
            horizontalItems(
                items = scheduleList,
                contentPadding = PaddingValues(horizontal = 16.dp),
                stickyHeader = { dayOfWeek ->
                    Column {
                        Text(
                            text = dayOfWeek.toLocalizedString(),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                },
                itemContent = { title ->
                    ReleaseCardItem(
                        release = title,
                        onClick = onReleaseClick,
                    )
                },
            )

            header(
                title = { Text(text = stringResource(Res.string.label_genres)) }
            )
            horizontalItems(
                items = genres,
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

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)
    val textHeight = with(LocalDensity.current) {
        MaterialTheme.typography.labelMedium.fontSize.toDp()
    }

    ProvideShimmer(shimmer) {
        Column(
            modifier = modifier
                .padding(contentPadding.only(WindowInsetsSides.Horizontal))
                .verticalScroll(rememberScrollState(), enabled = false)
        ) {
            ReleaseLargeCard(null) {
                ExpressiveButton(
                    onClick = {  },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Icon(
                        imageVector = AnilibriaIcons.Outlined.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(Res.string.button_watch))
                }
            }
            Header(
                title = { Text(stringResource(Res.string.label_schedule)) },
                onClick = {},
            )
            Spacer(Modifier.height(textHeight))
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState(), enabled = false)
                    .padding(horizontal = 16.dp)
            ) {
                repeat(10) {
                    ReleaseCardItem(release = null) {}
                }
            }
        }
    }
}
