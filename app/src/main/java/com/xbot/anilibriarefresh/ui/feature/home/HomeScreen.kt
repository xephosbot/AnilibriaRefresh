package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalLayoutDirection
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
import com.xbot.anilibriarefresh.ui.components.HorizontalPagerIndicator
import com.xbot.anilibriarefresh.ui.components.TitleCardItem
import com.xbot.anilibriarefresh.ui.components.TitleListItem
import com.xbot.anilibriarefresh.ui.components.TitlePagerItem
import com.xbot.anilibriarefresh.ui.utils.ProvideShimmer
import com.xbot.anilibriarefresh.ui.utils.shimmerUpdater
import com.xbot.anilibriarefresh.ui.utils.union
import com.xbot.domain.model.TitleModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    onNavigate: (Int) -> Unit
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
    onNavigate: (Int) -> Unit
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
                            favoriteList = successState.favoriteTitles,
                            contentPadding = innerPadding.union(paddingValues),
                            onTitleClick = onNavigate
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
    favoriteList: List<TitleModel>,
    contentPadding: PaddingValues,
    onTitleClick: (Int) -> Unit
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val pagerState = rememberPagerState(pageCount = { recommendedList.size })

    ProvideShimmer(shimmer) {
        LazyVerticalGrid (
            modifier = modifier.shimmerUpdater(shimmer),
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding
        ) {
            horizontalPagerItems(
                items = recommendedList,
                state = pagerState
            ) { title ->
                TitlePagerItem(title = title)
            }
            header(
                title = "Избранное",
                onClick = {} //TODO: On click action
            )
            horizontalItems(
                contentPadding = PaddingValues(horizontal = 16.dp),
                items = favoriteList
            ) { title ->
                TitleCardItem(title = title) {} //TODO: On click action
            }
            header(
                title = "Обновления",
                onClick = {} //TODO: On click action
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
    val layoutDirection = LocalLayoutDirection.current

    ProvideShimmer(shimmer) {
        Column(
            modifier = modifier
                .fillMaxSize()
                //TODO: переделать
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection)
                )
                .verticalScroll(rememberScrollState(), enabled = false),
        ) {
            TitlePagerItem(title = null)
            Header(title = "Избранное") { }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .horizontalScroll(rememberScrollState(), enabled = false)
            ) {
                repeat(4) {
                    TitleCardItem(title = null) {}
                }
            }
            Header(title = "Обновления")
            repeat(5) {
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

private fun LazyGridScope.header(
    title: String,
    onClick: () -> Unit
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "Header" }
    ) {
        Header(title = title, onClick = onClick)
    }
}

private fun LazyGridScope.horizontalPagerItems(
    items: List<TitleModel>,
    state: PagerState,
    itemContent: @Composable LazyGridItemScope.(TitleModel) -> Unit
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "PagerItems" }
    ) {
        Box(
            modifier = Modifier
                .heightIn(max = 400.dp)
                .clipToBounds()
        ) {
            HorizontalPager(state = state) { page ->
                itemContent(items[page])
            }
            HorizontalPagerIndicator(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                state = state
            )
        }
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
