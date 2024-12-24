package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.components.*
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.LazyRowWithStickyHeader
import com.xbot.anilibriarefresh.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.icons.Search
import com.xbot.designsystem.modifiers.ProvideShimmer
import com.xbot.designsystem.utils.only
import com.xbot.designsystem.modifiers.shimmerUpdater
import com.xbot.domain.models.Release
import kotlinx.datetime.DayOfWeek
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onNavigate: (Int, String) -> Unit,
) {
    val items = viewModel.titles.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        modifier = modifier,
        state = state,
        items = items,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    items: LazyPagingItems<Release>,
    onAction: (HomeScreenAction) -> Unit,
    onNavigate: (Int, String) -> Unit,
) {
    val showErrorMessage: (Throwable) -> Unit = { error ->
        onAction(HomeScreenAction.ShowErrorMessage(error) { items.retry() })
    }

    val pullToRefreshState = rememberPullToRefreshState()

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
            AnilibriaTopAppBar(
                modifier = Modifier,
                title = stringResource(R.string.home),
                navigationIcon = {
                    IconButton(
                        onClick = {},
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_anilibria),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {},
                    ) {
                        Icon(
                            imageVector = AnilibriaIcons.Outlined.Search,
                            contentDescription = null,
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Crossfade(
                targetState = state is HomeScreenState.Loading,
                label = "Loading state transition in HomeScreenContent",
            ) { loading ->
                when (loading) {
                    true -> LoadingScreen(contentPadding = innerPadding)
                    else -> {
                        val successState = state as HomeScreenState.Success
                        TitleList(
                            items = items,
                            recommendedList = successState.releasesFeed.recommendedReleases,
                            scheduleList = successState.releasesFeed.schedule,
                            contentPadding = innerPadding,
                            onTitleClick = { onNavigate(it.id, it.name) },
                        )
                    }
                }
            }

            Indicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(innerPadding.only(WindowInsetsSides.Top)),
                isRefreshing = isRefreshing,
                state = pullToRefreshState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Release>,
    recommendedList: List<Release>,
    scheduleList: Map<DayOfWeek, List<Release>>,
    contentPadding: PaddingValues,
    onTitleClick: (Release) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val carouselState = rememberCarouselState(itemCount = { recommendedList.size })

    ProvideShimmer(shimmer) {
        LazyVerticalGrid(
            modifier = modifier.shimmerUpdater(shimmer),
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding,
        ) {
            item(
                span = { GridItemSpan(maxLineSpan) },
                contentType = { HomeScreenContentType.Header },
            ) {
                Header(stringResource(R.string.schedule))
            }
            horizontalCarouselItems(state = carouselState) { index ->
                TitleCarouselItem(
                    release = recommendedList[index],
                    onClick = onTitleClick,
                )
            }
            item(
                span = { GridItemSpan(maxLineSpan) },
                contentType = { HomeScreenContentType.Header },
            ) {
                Header(
                    title = stringResource(R.string.schedule),
                    onClick = {}
                )
            }
            horizontalItems(
                items = scheduleList,
                contentPadding = PaddingValues(horizontal = 16.dp),
                stickyHeader = { dayOfWeek ->
                    Column {
                        // TODO: Use MaterialTheme.typography style
                        Text(
                            text = dayOfWeek.toStringRes(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            fontWeight = FontWeight.Normal,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                },
                itemContent = { title ->
                    TitleCardItem(
                        release = title,
                        onClick = onTitleClick,
                    )
                },
            )
            item(
                span = { GridItemSpan(maxLineSpan) },
                contentType = { HomeScreenContentType.Header },
            ) {
                Header(
                    title = stringResource(R.string.new_episodes),
                )
            }
            pagingItems(items) { title ->
                TitleListItem(
                    release = title,
                    onClick = onTitleClick,
                )
            }
        }
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
                .verticalScroll(rememberScrollState(), enabled = false)
                .padding(contentPadding),
        ) {
            LoadingCarouselItem()
            Spacer(modifier = Modifier.height(6.dp)) // Pager indicator size
            Spacer(modifier = Modifier.height(16.dp))
            Header(
                title = stringResource(R.string.schedule),
                onClick = {},
            )
            // TODO: Add extra spacing for way of week title
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState(), enabled = false)
                    .padding(horizontal = 16.dp),
            ) {
                repeat(6) {
                    TitleCardItem(release = null) {}
                }
            }
            Header(
                title = stringResource(R.string.new_episodes),
            )
            repeat(6) {
                TitleListItem(release = null)
            }
        }
    }
}

private fun LazyGridScope.horizontalItems(
    items: List<Release>,
    contentPadding: PaddingValues = PaddingValues(),
    itemContent: @Composable LazyItemScope.(Release) -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { HomeScreenContentType.HorizontalList },
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = contentPadding,
        ) {
            items(
                items = items,
                key = { it.id },
            ) {
                itemContent(it)
            }
        }
    }
}

private fun LazyGridScope.horizontalItems(
    items: Map<DayOfWeek, List<Release>>,
    contentPadding: PaddingValues = PaddingValues(),
    stickyHeader: @Composable (DayOfWeek) -> Unit,
    itemContent: @Composable LazyItemScope.(Release) -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { HomeScreenContentType.HorizontalList },
    ) {
        LazyRowWithStickyHeader(
            items = items,
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            stickyHeader = stickyHeader,
            itemContent = itemContent,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun LazyGridScope.horizontalCarouselItems(
    state: CarouselState,
    carouselContent: @Composable CarouselItemScope.(index: Int) -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { HomeScreenContentType.CarouselItems },
    ) {
        HorizontalMultiBrowseCarousel(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = state,
            preferredItemWidth = 312.dp,
            itemSpacing = 16.dp
        ) { index ->
            carouselContent(index)
        }
    }
}

private fun LazyGridScope.pagingItems(
    items: LazyPagingItems<Release>,
    itemContent: @Composable LazyGridItemScope.(Release?) -> Unit,
) {
    items(
        count = items.itemCount,
        key = items.itemKey(),
        contentType = items.itemContentType { HomeScreenContentType.PagingItems },
    ) {
        itemContent(items[it])
    }
}

@Composable
private fun DayOfWeek.toStringRes(): String = when (this) {
    DayOfWeek.MONDAY -> stringResource(R.string.monday)
    DayOfWeek.TUESDAY -> stringResource(R.string.tuesday)
    DayOfWeek.WEDNESDAY -> stringResource(R.string.wednesday)
    DayOfWeek.THURSDAY -> stringResource(R.string.thursday)
    DayOfWeek.FRIDAY -> stringResource(R.string.friday)
    DayOfWeek.SATURDAY -> stringResource(R.string.saturday)
    DayOfWeek.SUNDAY -> stringResource(R.string.sunday)
}

private enum class HomeScreenContentType {
    Header,
    HorizontalList,
    CarouselItems,
    PagingItems
}
