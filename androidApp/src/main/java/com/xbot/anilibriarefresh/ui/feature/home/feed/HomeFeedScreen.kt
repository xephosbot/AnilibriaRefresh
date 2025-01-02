package com.xbot.anilibriarefresh.ui.feature.home.feed

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
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
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.LoadingReleasePagerItem
import com.xbot.designsystem.components.ReleaseCardItem
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.ReleasePagerItem
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.horizontalPagerItems
import com.xbot.designsystem.components.listItemShape
import com.xbot.designsystem.modifiers.ProvideShimmer
import com.xbot.designsystem.modifiers.shimmerUpdater
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Release
import kotlinx.datetime.DayOfWeek
import org.koin.androidx.compose.koinViewModel
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeFeedScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeFeedViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onReleaseClick: (Int) -> Unit,
) {
    val items = viewModel.releases.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeFeedScreenContent(
        modifier = modifier,
        contentPadding = contentPadding,
        state = state,
        items = items,
        onAction = viewModel::onAction,
        onReleaseClick = onReleaseClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeFeedScreenContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    state: HomeFeedScreenState,
    items: LazyPagingItems<Release>,
    onAction: (HomeFeedScreenAction) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val showErrorMessage: (Throwable) -> Unit = { error ->
        onAction(HomeFeedScreenAction.ShowErrorMessage(error) { items.retry() })
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
        derivedStateOf { items.loadState.refresh == LoadState.Loading || state is HomeFeedScreenState.Loading }
    }

    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = {
            items.refresh()
            onAction(HomeFeedScreenAction.Refresh)
        },
        state = pullToRefreshState,
        indicator = {
            Indicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(contentPadding.only(WindowInsetsSides.Top)),
                isRefreshing = isRefreshing,
                state = pullToRefreshState
            )
        }
    ) {
        Crossfade(
            targetState = state,
            label = "Loading state transition in HomeScreenContent",
        ) { targetState ->
            when (targetState) {
                is HomeFeedScreenState.Loading -> LoadingScreen()
                is HomeFeedScreenState.Success -> {
                    TitleList(
                        items = items,
                        recommendedList = targetState.releasesFeed.recommendedReleases,
                        scheduleList = targetState.releasesFeed.schedule,
                        onReleaseClick = { onReleaseClick(it.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Release>,
    recommendedList: List<Release>,
    scheduleList: Map<DayOfWeek, List<Release>>,
    onReleaseClick: (Release) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val pagerState = rememberPagerState(pageCount = { recommendedList.size })

    ProvideShimmer(shimmer) {
        LazyVerticalGrid(
            modifier = modifier.shimmerUpdater(shimmer),
            columns = GridCells.Adaptive(350.dp),
        ) {
            horizontalPagerItems(state = pagerState) { index ->
                ReleasePagerItem(
                    release = recommendedList[index]
                ) {
                    Button(
                        onClick = {}
                    ) {
                        Text(text = stringResource(R.string.button_watch))
                    }
                }
            }
            item(
                span = { GridItemSpan(maxLineSpan) },
                contentType = { HomeScreenContentType.Header },
            ) {
                Header(
                    title = stringResource(R.string.label_schedule),
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
                            text = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                },
                itemContent = { title ->
                    ReleaseCardItem(
                        release = title,
                        onClick = onReleaseClick,
                    )
                },
            )
            item(
                span = { GridItemSpan(maxLineSpan) },
                contentType = { HomeScreenContentType.Header },
            ) {
                Header(
                    title = stringResource(R.string.label_new_episodes),
                )
            }
            pagingItems(items) { index, release ->
                Column {
                    ReleaseListItem(
                        modifier = Modifier
                            .clip(listItemShape(index, items.itemCount - 1)),
                        release = release,
                        onClick = onReleaseClick,
                    )
                    if (index < items.itemCount - 1) {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    ProvideShimmer(shimmer) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState(), enabled = false)
        ) {
            LoadingReleasePagerItem()
            Header(
                title = stringResource(R.string.label_schedule),
                onClick = {},
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState(), enabled = false)
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp),
            ) {
                repeat(6) {
                    ReleaseCardItem(release = null) {}
                }
            }
            Header(
                title = stringResource(R.string.label_new_episodes),
            )
            repeat(6) {
                ReleaseListItem(release = null)
                Spacer(Modifier.height(8.dp))
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
        contentType = items.itemContentType { HomeScreenContentType.PagingItems },
    ) {
        itemContent(it, items[it])
    }
}

private enum class HomeScreenContentType {
    Header,
    PagingItems
}
