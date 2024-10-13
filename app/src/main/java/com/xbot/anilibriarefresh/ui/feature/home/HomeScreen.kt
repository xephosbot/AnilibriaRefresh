package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.valentinilk.shimmer.unclippedBoundsInWindow
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
        refreshLoadState = lazyTitlesItems.loadState.refresh,
        appendLoadState = lazyTitlesItems.loadState.append,
        prependLoadState = lazyTitlesItems.loadState.prepend,
        onAction = viewModel::onAction,
        onNavigate = onNavigate
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    items: LazyPagingItems<TitleModel>,
    refreshLoadState: LoadState,
    appendLoadState: LoadState,
    prependLoadState: LoadState,
    onAction: (HomeScreenAction) -> Unit,
    onNavigate: (Int) -> Unit
) {
    val showErrorMessage: (Throwable) -> Unit = { error ->
        onAction(HomeScreenAction.ShowErrorMessage(error) { items.retry() })
    }

    when {
        refreshLoadState is LoadState.Error -> showErrorMessage(refreshLoadState.error)
        appendLoadState is LoadState.Error -> showErrorMessage(appendLoadState.error)
        prependLoadState is LoadState.Error -> showErrorMessage(prependLoadState.error)
    }

    Scaffold(modifier = modifier) { innerPadding ->
        Crossfade(
            targetState = refreshLoadState,
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

    LazyColumn(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                val position = layoutCoordinates.unclippedBoundsInWindow()
                shimmer.updateBounds(position)
            },
        contentPadding = contentPadding
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey(),
            contentType = items.itemContentType { "TitleItem" }
        ) { index ->
            TitleItem(
                title = items[index],
                shimmer = shimmer,
                onClick = onTitleClick
            )
        }
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(top = contentPadding.calculateTopPadding())
            .verticalScroll(rememberScrollState(), enabled = false),
    ) {
        repeat(5) {
            TitleItem(
                title = null,
                shimmer = shimmer
            )
        }
    }
}
