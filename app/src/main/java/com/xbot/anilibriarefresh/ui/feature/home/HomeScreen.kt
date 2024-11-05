package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
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
import com.xbot.anilibriarefresh.ui.utils.shimmerUpdater
import com.xbot.anilibriarefresh.ui.utils.union
import com.xbot.domain.model.DayOfWeek
import com.xbot.domain.model.TitleModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    onNavigate: (Int, String) -> Unit
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
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    state: HomeScreenState,
    items: LazyPagingItems<TitleModel>,
    loadStates: CombinedLoadStates,
    onAction: (HomeScreenAction) -> Unit,
    onNavigate: (Int, String) -> Unit
) {
    val showErrorMessage: (Throwable) -> Unit = { error ->
        onAction(HomeScreenAction.ShowErrorMessage(error) { items.retry() })
    }

    when {
        (loadStates.refresh is LoadState.Error) -> showErrorMessage((loadStates.refresh as LoadState.Error).error)
        (loadStates.append is LoadState.Error) -> showErrorMessage((loadStates.append as LoadState.Error).error)
        (loadStates.prepend is LoadState.Error) -> showErrorMessage((loadStates.prepend as LoadState.Error).error)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = loadStates.refresh is LoadState.Loading,
        onRefresh = items::refresh
    )

    Scaffold(modifier = modifier) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            Crossfade(
                targetState = loadStates.refresh is LoadState.Loading || state is HomeScreenState.Loading,
                label = "" //TODO: информативный label для перехода
            ) { targetState ->
                when (targetState) {
                    true -> LoadingScreen(
                        contentPadding = innerPadding.union(paddingValues)
                    )
                    else -> {
                        val successState = state as HomeScreenState.Success
                        TitleList(
                            items = items,
                            recommendedList = successState.recommendedTitles,
                            scheduleList = successState.scheduleTitles,
                            contentPadding = innerPadding.union(paddingValues),
                            onTitleClick = { onNavigate(it.id, it.name) }
                        )
                    }
                }
            }

            //I wrapped it in a box because I needed to add padding.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding.union(paddingValues))
            ) {
                PullRefreshIndicator(
                    refreshing = loadStates.refresh is LoadState.Loading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
private fun TitleList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<TitleModel>,
    recommendedList: List<TitleModel>,
    scheduleList: Map<DayOfWeek, List<TitleModel>>,
    contentPadding: PaddingValues,
    onTitleClick: (TitleModel) -> Unit
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val pagerState = rememberPagerState(pageCount = { recommendedList.size })

    ProvideShimmer(shimmer) {
        LazyVerticalGrid (
            modifier = modifier.shimmerUpdater(shimmer),
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding
        ) {
            horizontalPagerItems(state = pagerState) { page ->
                TitlePagerItem(
                    modifier = Modifier.pagerItemTransition(page, pagerState),
                    title = recommendedList[page],
                    onClick = onTitleClick
                )
            }
            header(
                title = "Расписание", //TODO: Move to string resources
            )
            horizontalItems(
                items = scheduleList,
                contentPadding = PaddingValues(horizontal = 16.dp),
                stickyHeader = { dayOfWeek ->
                    Column {
                        Text(
                            text = dayOfWeek.toStringRes()
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                },
                itemContent = { title ->
                    TitleCardItem(
                        title = title,
                        onClick = onTitleClick
                    )
                }
            )
            header(
                title = "Новые эпизоды", //TODO: Move to string resources
                contentPadding = HeaderDefaults.ContentPaddingExcludeBottom
            )
            pagingItems(items) { title ->
                TitleListItem(
                    title = title,
                    onClick = onTitleClick
                )
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

    ProvideShimmer(shimmer) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState(), enabled = false)
                .padding(contentPadding),
        ) {
            LoadingPagerItem()
            Spacer(modifier = Modifier.height(6.dp)) //Pager indicator size
            Spacer(modifier = Modifier.height(16.dp))
            Header(title = "Расписание") //TODO: Move to string resources
            //TODO: Add extra spacing for way of week title
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState(), enabled = false)
                    .padding(horizontal = 16.dp)
            ) {
                repeat(6) {
                    TitleCardItem(title = null) {}
                }
            }
            Header(
                title = "Новые эпизоды", //TODO: Move to string resources
                contentPadding = HeaderDefaults.ContentPaddingExcludeBottom
            )
            repeat(6) {
                TitleListItem(title = null)
            }
        }
    }
}

private fun LazyGridScope.horizontalItems(
    items: List<TitleModel>,
    contentPadding: PaddingValues = PaddingValues(),
    itemContent: @Composable LazyItemScope.(TitleModel) -> Unit
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "HorizontalList" }
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = contentPadding
        ) {
            items(
                items = items,
                key = { it.id }
            ) {
                itemContent(it)
            }
        }
    }
}

private fun LazyGridScope.horizontalItems(
    items: Map<DayOfWeek, List<TitleModel>>,
    contentPadding: PaddingValues = PaddingValues(),
    stickyHeader: @Composable (DayOfWeek) -> Unit,
    itemContent: @Composable LazyItemScope.(TitleModel) -> Unit
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "HorizontalList" }
    ) {
        LazyRowWithStickyHeader(
            items = items,
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            stickyHeader = stickyHeader,
            itemContent = itemContent
        )
    }
}

private fun LazyGridScope.horizontalPagerItems(
    state: PagerState,
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "PagerItems" }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = state,
                beyondViewportPageCount = 1
            ) { page ->
                pageContent(page)
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalPagerIndicator(state = state)
        }
    }
}

private fun LazyGridScope.header(
    title: String,
    contentPadding: PaddingValues = HeaderDefaults.ContentPadding,
    onClick: (() -> Unit)? = null
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "Header" }
    ) {
        Header(
            title = title,
            contentPadding = contentPadding,
            onClick = onClick
        )
    }
}

private fun LazyGridScope.pagingItems(
    items: LazyPagingItems<TitleModel>,
    itemContent: @Composable LazyGridItemScope.(TitleModel?) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(),
        contentType = items.itemContentType { "PagingItems" }
    ) {
        itemContent(items[it])
    }
}

//TODO: Move to string resources
private fun DayOfWeek.toStringRes(): String = when(this) {
    DayOfWeek.MONDAY -> "Понедельник"
    DayOfWeek.TUESDAY -> "Вторник"
    DayOfWeek.WEDNESDAY -> "Среда"
    DayOfWeek.THURSDAY -> "Четверг"
    DayOfWeek.FRIDAY -> "Пятница"
    DayOfWeek.SATURDAY -> "Суббота"
    DayOfWeek.SUNDAY -> "Воскресенье"
}