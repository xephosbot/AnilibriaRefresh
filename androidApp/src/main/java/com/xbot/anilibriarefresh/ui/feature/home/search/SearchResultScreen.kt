package com.xbot.anilibriarefresh.ui.feature.home.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.listItemShape
import com.xbot.designsystem.modifiers.ProvideShimmer
import com.xbot.designsystem.modifiers.shimmerUpdater
import com.xbot.domain.models.Release
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchResultViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onReleaseClick: (Int) -> Unit,
) {
    val items = viewModel.releases.collectAsLazyPagingItems()

    SearchScreenResultContent(
        modifier = modifier,
        items = items,
        contentPadding = contentPadding,
        onAction = viewModel::onAction,
        onReleaseClick = onReleaseClick
    )
}

@Composable
private fun SearchScreenResultContent(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Release>,
    contentPadding: PaddingValues,
    onAction: (SearchResultScreenAction) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val showErrorMessage: (Throwable) -> Unit = { error ->
        onAction(SearchResultScreenAction.ShowErrorMessage(error) { items.retry() })
    }

    LaunchedEffect(items) {
        snapshotFlow { items.loadState }.collect { loadState ->
            (loadState.refresh as? LoadState.Error)?.let { showErrorMessage(it.error) }
            (loadState.append as? LoadState.Error)?.let { showErrorMessage(it.error) }
            (loadState.prepend as? LoadState.Error)?.let { showErrorMessage(it.error) }
        }
    }

    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    ProvideShimmer(shimmer) {
        LazyVerticalGrid(
            modifier = modifier.shimmerUpdater(shimmer),
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding
        ) {
            item {
                Spacer(Modifier.height(16.dp))
            }
            pagingItems(items) { index, release ->
                Column {
                    ReleaseListItem(
                        modifier = Modifier
                            .clip(listItemShape(index, items.itemCount - 1)),
                        release = release,
                        onClick = { onReleaseClick(it.id) },
                    )
                    if (index < items.itemCount - 1) {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

private fun LazyGridScope.pagingItems(
    items: LazyPagingItems<Release>,
    itemContent: @Composable LazyGridItemScope.(index: Int, item: Release?) -> Unit,
) {
    items(
        count = items.itemCount,
        key = items.itemKey(),
        contentType = items.itemContentType { SearchResultScreenContentType.PagingItems },
    ) {
        itemContent(it, items[it])
    }
}

private enum class SearchResultScreenContentType {
    PagingItems
}
