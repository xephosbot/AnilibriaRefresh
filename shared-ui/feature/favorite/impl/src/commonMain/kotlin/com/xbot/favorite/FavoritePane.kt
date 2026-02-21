package com.xbot.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.SectionDefaults
import com.xbot.designsystem.components.pagingItems
import com.xbot.designsystem.components.section
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Release
import com.xbot.fixtures.data.releaseMocks
import com.xbot.resources.Res
import com.xbot.resources.tab_favorite
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun FavoritePane(
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = koinViewModel(),
    onReleaseClick: (Release) -> Unit
) {
    val items = viewModel.favoriteReleases.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    FavoritePaneContent(
        modifier = modifier,
        state = state,
        items = items,
        onAction = viewModel::onAction,
        onReleaseClick = onReleaseClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FavoritePaneContent(
    modifier: Modifier = Modifier,
    state: FavoriteScreenState,
    items: LazyPagingItems<Release>,
    onAction: (FavoriteScreenAction) -> Unit,
    onReleaseClick: (Release) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val gridState = rememberLazyGridState()
    val pullToRefreshState = rememberPullToRefreshState()
    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    val columnsCount = remember {
        derivedStateOf { gridState.layoutInfo.maxSpan }
    }

    val onShowErrorMessage: (Throwable) -> Unit = { error ->
        onAction(FavoriteScreenAction.ShowErrorMessage(error) { items.retry() })
    }

    LaunchedEffect(items) {
        snapshotFlow { items.loadState }.collect { loadState ->
            (loadState.refresh as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
            (loadState.append as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
            (loadState.prepend as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
        }
    }

    val isRefreshing by remember(state, items) {
        derivedStateOf { items.loadState.refresh == LoadState.Loading }
    }

    ProvideShimmer(shimmer) {
        Scaffold(
            modifier = modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .pullToRefresh(
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState,
                    onRefresh = {
                        items.refresh()
                        onAction(FavoriteScreenAction.Refresh)
                    }
                )
                .shimmerUpdater(shimmer),
            topBar = {
                LargeFlexibleTopAppBar(
                    title = { Text(text = stringResource(Res.string.tab_favorite)) },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainer)
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ) { innerPadding ->
            Box {
                Feed(
                    modifier = Modifier.fillMaxSize(),
                    state = gridState,
                    columns = GridCells.Adaptive(400.dp),
                    contentPadding = innerPadding.only(WindowInsetsSides.Bottom + WindowInsetsSides.Top)
                ) {
                    pagingItems(
                        items = items,
                        loadingPlaceholderCount = 10,
                    ) { index, release ->
                        ReleaseListItem(
                            modifier = Modifier
                                .section(
                                    index = index,
                                    itemsCount = items.itemCount,
                                    columnsCount = columnsCount.value,
                                    sectionSpacing = SectionDefaults.spacing(
                                        contentPadding = innerPadding.only(WindowInsetsSides.Horizontal)
                                    )
                                ),
                            release = release,
                            onClick = onReleaseClick,
                        )
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
}

@Preview
@Composable
private fun FavoritePanePreview() {
    AnilibriaPreview {
        val items = flowOf(PagingData.from(releaseMocks)).collectAsLazyPagingItems()
        FavoritePaneContent(
            state = FavoriteScreenState(),
            items = items,
            onAction = {},
            onReleaseClick = {}
        )
    }
}
