package com.xbot.home

import androidx.compose.animation.Crossfade
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.GenreItem
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.ReleaseCardItem
import com.xbot.designsystem.components.ReleaseLargeCard
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.horizontalPagerItems
import com.xbot.designsystem.components.pagingItems
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.AnilibriaLogoLarge
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.fadeWithParallax
import com.xbot.designsystem.modifier.horizontalParallax
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.modifier.verticalParallax
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.resources.*
import com.xbot.localization.toLocalizedString
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onSearchClick: () -> Unit,
    onScheduleClick: () -> Unit,
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
        onScheduleClick = onScheduleClick,
        onReleaseClick = onReleaseClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    items: LazyPagingItems<Release>,
    onAction: (HomeScreenAction) -> Unit,
    onSearchClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val showErrorMessage: (Throwable) -> Unit = { error ->
        onAction(HomeScreenAction.ShowErrorMessage(error) { items.retry() })
    }

    val pullToRefreshState = rememberPullToRefreshState()
    val gridState = rememberLazyGridState()

    var topAppbarHeight by remember { mutableStateOf(0) }
    val topAppBarAlpha by remember {
        derivedStateOf {
            if (gridState.layoutInfo.visibleItemsInfo.isEmpty()) {
                0f
            } else if (gridState.firstVisibleItemIndex == 0) {
                gridState.layoutInfo.visibleItemsInfo.first().let {
                    (it.offset.y / (it.size.height - topAppbarHeight).toFloat()).absoluteValue
                }
            } else {
                1f
            }
        }
    }

    LaunchedEffect(items) {
        snapshotFlow { items.loadState }.collect { loadState ->
            (loadState.refresh as? LoadState.Error)?.let { showErrorMessage(it.error) }
            (loadState.append as? LoadState.Error)?.let { showErrorMessage(it.error) }
            (loadState.prepend as? LoadState.Error)?.let { showErrorMessage(it.error) }
        }
    }

    val isRefreshing by remember(state, items) {
        derivedStateOf { items.loadState.refresh == LoadState.Loading || state is HomeScreenState.Loading }
    }

    Scaffold(
        modifier = modifier.pullToRefresh(
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            onRefresh = {
                items.refresh()
                onAction(HomeScreenAction.Refresh)
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
                    Row {
                        Button(
                            onClick = { onReleaseClick(release) },
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.onTertiary
                            )
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
                        FilledTonalIconButton(
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
                Button(
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
