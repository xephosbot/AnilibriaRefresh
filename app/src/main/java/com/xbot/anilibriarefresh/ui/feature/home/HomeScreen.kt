package com.xbot.anilibriarefresh.ui.feature.home
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.xbot.domain.model.TitleModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (Int) -> Unit
) {
    val lazyTitlesItems = viewModel.titles.collectAsLazyPagingItems()

    HomeScreenContent(
        modifier = modifier,
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

    Scaffold(modifier = modifier,) { innerPadding ->
        Crossfade(
            targetState = refreshLoadState,
            label = "" //TODO: информативный label для перехода
        ) { state ->
            when(state) {
                is LoadState.Loading -> LoadingScreen()
                else -> TitleList(
                    items = items,
                    contentPadding = innerPadding,
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
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey(),
            contentType = items.itemContentType { "TitleItem" }
        ) { index ->
            TitleItem(
                modifier = Modifier.animateItem(),
                title = items[index],
                onClick = onTitleClick
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}
