package com.xbot.anilibriarefresh.ui.feature.home

import androidx.annotation.StringRes
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
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
import com.xbot.anilibriarefresh.ui.components.Header
import com.xbot.anilibriarefresh.ui.components.HeaderDefaults
import com.xbot.anilibriarefresh.ui.components.HorizontalPagerIndicator
import com.xbot.anilibriarefresh.ui.components.LazyRowWithStickyHeader
import com.xbot.anilibriarefresh.ui.components.LoadingPagerItem
import com.xbot.anilibriarefresh.ui.components.TitleCardItem
import com.xbot.anilibriarefresh.ui.components.TitleListItem
import com.xbot.anilibriarefresh.ui.components.TitlePagerItem
import com.xbot.anilibriarefresh.ui.components.pagerItemTransition
import com.xbot.anilibriarefresh.ui.utils.ProvideShimmer
import com.xbot.anilibriarefresh.ui.utils.only
import com.xbot.anilibriarefresh.ui.utils.shimmerUpdater
import com.xbot.anilibriarefresh.ui.utils.union
import com.xbot.domain.models.enums.DayOfWeek
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    paddingValues: PaddingValues,
    onNavigate: (Int, String) -> Unit,
) {
    val items = viewModel.titles.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        modifier = modifier,
        paddingValues = paddingValues,
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
    paddingValues: PaddingValues,
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

    Scaffold(
        modifier = modifier.pullToRefresh(
            isRefreshing = loadStates.refresh is LoadState.Loading,
            state = pullToRefreshState,
            onRefresh = items::refresh
        )
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Crossfade(
                targetState = loadStates.refresh is LoadState.Loading || state is HomeScreenState.Loading,
                label = "Loading state transition in HomeScreenContent",
            ) { targetState ->
                when (targetState) {
                    true -> LoadingScreen(
                        contentPadding = innerPadding.union(paddingValues),
                    )

                    else -> {
                        val successState = state as HomeScreenState.Success
                        TitleList(
                            items = items,
                            recommendedList = successState.recommendedTitles,
                            scheduleList = successState.scheduleTitles,
                            contentPadding = innerPadding.union(paddingValues),
                            onTitleClick = { onNavigate(it.id, it.name) },
                        )
                    }
                }
            }

            Indicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        paddingValues = innerPadding
                            .union(paddingValues)
                            .only(WindowInsetsSides.Top)
                    ),
                isRefreshing = loadStates.refresh is LoadState.Loading,
                state = pullToRefreshState
            )
        }
    }
}

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
    val pagerState = rememberPagerState(pageCount = { recommendedList.size })

    ProvideShimmer(shimmer) {
        LazyVerticalGrid(
            modifier = modifier.shimmerUpdater(shimmer),
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding,
        ) {
            horizontalPagerItems(state = pagerState) { page ->
                TitlePagerItem(
                    modifier = Modifier.pagerItemTransition(page, pagerState),
                    title = recommendedList[page],
                    onClick = onTitleClick,
                )
            }
            header(
                titleResId = R.string.schedule,
                onClick = {},
            )
            horizontalItems(
                items = scheduleList,
                contentPadding = PaddingValues(horizontal = 16.dp),
                stickyHeader = { dayOfWeek ->
                    Column {
                        Text(
                            text = dayOfWeek.toStringRes(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            style = LocalTextStyle.current.copy(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false,
                                ),
                            ),
                            fontWeight = FontWeight.SemiBold,
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
            header(
                titleResId = R.string.new_episodes,
                contentPadding = HeaderDefaults.ContentPaddingExcludeBottom,
            )
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
            LoadingPagerItem()
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
                contentPadding = HeaderDefaults.ContentPaddingExcludeBottom,
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
        contentType = { "HorizontalList" },
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
        contentType = { "HorizontalList" },
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

private fun LazyGridScope.horizontalPagerItems(
    state: PagerState,
    pageContent: @Composable PagerScope.(page: Int) -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "PagerItems" },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalPager(
                state = state,
                beyondViewportPageCount = 1,
            ) { page ->
                pageContent(page)
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalPagerIndicator(state = state)
        }
    }
}

private fun LazyGridScope.header(
    @StringRes titleResId: Int,
    contentPadding: PaddingValues = HeaderDefaults.ContentPadding,
    onClick: (() -> Unit)? = null,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "Header" },
    ) {
        Header(
            title = stringResource(id = titleResId),
            contentPadding = contentPadding,
            onClick = onClick,
        )
    }
}

private fun LazyGridScope.pagingItems(
    items: LazyPagingItems<Title>,
    itemContent: @Composable LazyGridItemScope.(Title?) -> Unit,
) {
    items(
        count = items.itemCount,
        key = items.itemKey(),
        contentType = items.itemContentType { "PagingItems" },
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
