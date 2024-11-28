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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.models.Title
import com.xbot.anilibriarefresh.ui.components.AnilibriaTopAppBar
import com.xbot.anilibriarefresh.ui.components.Header
import com.xbot.anilibriarefresh.ui.components.LazyRowWithStickyHeader
import com.xbot.anilibriarefresh.ui.components.LoadingCarouselItem
import com.xbot.anilibriarefresh.ui.components.Scaffold
import com.xbot.anilibriarefresh.ui.components.TitleCardItem
import com.xbot.anilibriarefresh.ui.components.TitleCarouselItem
import com.xbot.anilibriarefresh.ui.components.TitleListItem
import com.xbot.anilibriarefresh.ui.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.ui.icons.Search
import com.xbot.anilibriarefresh.ui.utils.ProvideShimmer
import com.xbot.anilibriarefresh.ui.utils.only
import com.xbot.anilibriarefresh.ui.utils.shimmerUpdater
import com.xbot.domain.models.enums.DayOfWeek
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.LocalHazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
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
        loadStates = items.loadState,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    items: LazyPagingItems<Title>,
    loadStates: CombinedLoadStates,
    onAction: (HomeScreenAction) -> Unit,
    onNavigate: (Int, String) -> Unit,
) {
    val showErrorMessage: (Throwable) -> Unit = { error ->
        onAction(HomeScreenAction.ShowErrorMessage(error) { items.retry() })
    }

    when {
        (loadStates.refresh is LoadState.Error) -> showErrorMessage((loadStates.refresh as LoadState.Error).error)
        (loadStates.append is LoadState.Error) -> showErrorMessage((loadStates.append as LoadState.Error).error)
        (loadStates.prepend is LoadState.Error) -> showErrorMessage((loadStates.prepend as LoadState.Error).error)
    }

    val pullToRefreshState = rememberPullToRefreshState()
    val hazeState = remember { HazeState() }

    Scaffold(
        modifier = modifier.pullToRefresh(
            isRefreshing = loadStates.refresh is LoadState.Loading,
            state = pullToRefreshState,
            onRefresh = {
                items.refresh()
                onAction(HomeScreenAction.Refresh)
            }
        ),
        topBar = {
            AnilibriaTopAppBar(
                modifier = Modifier
                    .hazeChild(
                        state = hazeState,
                        style = LocalHazeStyle.current,
                    ),
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .haze(hazeState)
        ) {
            Crossfade(
                targetState = loadStates.refresh is LoadState.Loading || state is HomeScreenState.Loading,
                label = "Loading state transition in HomeScreenContent",
            ) { targetState ->
                when (targetState) {
                    true -> LoadingScreen(
                        contentPadding = innerPadding,
                    )

                    else -> {
                        val successState = state as HomeScreenState.Success
                        TitleList(
                            items = items,
                            recommendedList = successState.recommendedTitles,
                            scheduleList = successState.scheduleTitles,
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
                isRefreshing = loadStates.refresh is LoadState.Loading,
                state = pullToRefreshState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Title>,
    recommendedList: List<Title>,
    scheduleList: Map<DayOfWeek, List<Title>>,
    contentPadding: PaddingValues,
    onTitleClick: (Title) -> Unit,
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
                    title = recommendedList[index],
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
                        title = title,
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
                    title = title,
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
                    TitleCardItem(title = null) {}
                }
            }
            Header(
                title = stringResource(R.string.new_episodes),
            )
            repeat(6) {
                TitleListItem(title = null)
            }
        }
    }
}

private fun LazyGridScope.horizontalItems(
    items: List<Title>,
    contentPadding: PaddingValues = PaddingValues(),
    itemContent: @Composable LazyItemScope.(Title) -> Unit,
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
    items: Map<DayOfWeek, List<Title>>,
    contentPadding: PaddingValues = PaddingValues(),
    stickyHeader: @Composable (DayOfWeek) -> Unit,
    itemContent: @Composable LazyItemScope.(Title) -> Unit,
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
    items: LazyPagingItems<Title>,
    itemContent: @Composable LazyGridItemScope.(Title?) -> Unit,
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
