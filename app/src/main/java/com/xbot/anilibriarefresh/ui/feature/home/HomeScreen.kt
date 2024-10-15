package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.unclippedBoundsInWindow
import com.xbot.anilibriarefresh.ui.components.LocalShimmer
import com.xbot.anilibriarefresh.ui.components.TitleItem
import com.xbot.anilibriarefresh.ui.utils.union
import com.xbot.domain.model.TitleModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    onNavigate: (Int) -> Unit
) {
    val lazyTitlesItems = viewModel.titles.collectAsLazyPagingItems()

    HomeScreenContent(
        modifier = modifier,
        paddingValues = paddingValues,
        items = lazyTitlesItems,
        loadStates = lazyTitlesItems.loadState,
        onAction = viewModel::onAction,
        onNavigate = onNavigate
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
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

    Scaffold(modifier = modifier) { innerPadding ->
        Crossfade(
            targetState = loadStates.refresh,
            label = "" //TODO: информативный label для перехода
        ) { state ->
            when(state) {
                is LoadState.Loading -> LoadingScreen(
                    contentPadding = innerPadding.union(paddingValues)
                )
                else -> TitleList(
                    items = items,
                    contentPadding = innerPadding.union(paddingValues),
                    onTitleClick = onNavigate
                )
            }
        }
    }
}

@Composable
private fun TitleList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<TitleModel>,
    contentPadding: PaddingValues,
    onTitleClick: (Int) -> Unit
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val pagerState = rememberPagerState(pageCount = { 5 }) //TODO: update pager state

    CompositionLocalProvider(LocalShimmer provides shimmer) {
        LazyColumn(
            modifier = modifier
                .onGloballyPositioned { layoutCoordinates ->
                    val position = layoutCoordinates.unclippedBoundsInWindow()
                    shimmer.updateBounds(position)
                },
            contentPadding = contentPadding
        ) {
            horizontalPagerItems(
                items = listOf(),
                state = pagerState
            ) { title ->
                //TODO: Pager item element
            }
            header(
                title = "Избранное",
                onClick = {} //TODO: On click action
            )
            horizontalItems(listOf()) { title ->
                //TODO: Horizontal list item element
            }
            header(
                title = "Обновления",
                onClick = {} //TODO: On click action
            )
            pagingItems(items) { title ->
                TitleItem(
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

    CompositionLocalProvider(LocalShimmer provides shimmer) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
                .verticalScroll(rememberScrollState(), enabled = false),
        ) {
            //TODO: добавить в загрузочный placeholder все элементы как на главном экране
            repeat(5) {
                TitleItem(title = null)
            }
        }
    }
}

private fun LazyListScope.horizontalItems(
    items: List<TitleModel>,
    contentPadding: PaddingValues = PaddingValues(),
    itemContent: @Composable LazyItemScope.(TitleModel) -> Unit
) {
    item(
        contentType = { "HorizontalList" }
    ) {
        LazyRow(contentPadding = contentPadding) {
            items(
                items = items,
                key = { it.id }
            ) {
                itemContent(it)
            }
        }
    }
}

private fun LazyListScope.header(
    title: String,
    onClick: () -> Unit
) {
    item(
        contentType = { "Header" }
    ) {
        //TODO: Header
    }
}

private fun LazyListScope.horizontalPagerItems(
    items: List<TitleModel>,
    state: PagerState,
    itemContent: @Composable LazyItemScope.(TitleModel) -> Unit
) {
    item(
        contentType = { "PagerItems" }
    ) {
        HorizontalPager(state = state) { page ->
            itemContent(items[page])
        }
    }
}

private fun LazyListScope.pagingItems(
    items: LazyPagingItems<TitleModel>,
    itemContent: @Composable LazyItemScope.(TitleModel?) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(),
        contentType = items.itemContentType { "PagingItems" }
    ) {
        itemContent(items[it])
    }
}
