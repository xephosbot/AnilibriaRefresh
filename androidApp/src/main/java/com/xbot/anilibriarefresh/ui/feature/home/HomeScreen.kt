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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import com.xbot.anilibriarefresh.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.icons.Search
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
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onNavigate: (Int) -> Unit,
) {
    val items = viewModel.releases.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        modifier = modifier,
        state = state,
        items = items,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    items: LazyPagingItems<Release>,
    onAction: (HomeScreenAction) -> Unit,
    onNavigate: (Int) -> Unit,
) {
    val showErrorMessage: (Throwable) -> Unit = { error ->
        onAction(HomeScreenAction.ShowErrorMessage(error) { items.retry() })
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
        derivedStateOf { items.loadState.refresh == LoadState.Loading || state is HomeScreenState.Loading }
    }

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchBarExpanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.pullToRefresh(
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            enabled = !searchBarExpanded,
            onRefresh = {
                items.refresh()
                onAction(HomeScreenAction.Refresh)
            }
        ),
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onSearch = { searchBarExpanded = false },
                            expanded = searchBarExpanded,
                            onExpandedChange = { searchBarExpanded = it },
                            placeholder = {
                                Text(text = stringResource(R.string.search_bar_placeholder))
                            },
                            leadingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_anilibria),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = AnilibriaIcons.Outlined.Search,
                                    contentDescription = null
                                )
                            }
                        )
                    },
                    expanded = searchBarExpanded,
                    onExpandedChange = { searchBarExpanded = it },
                    content = {
                        FiltersScreen(
                            filters = (state as? HomeScreenState.Success)?.filters
                        )
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val successState = state as? HomeScreenState.Success

            Crossfade(
                targetState = successState,
                label = "Loading state transition in HomeScreenContent",
            ) { targetState ->
                when (targetState) {
                    null -> LoadingScreen(contentPadding = PaddingValues())

                    else -> {
                        TitleList(
                            items = items,
                            recommendedList = targetState.releasesFeed.recommendedReleases,
                            scheduleList = targetState.releasesFeed.schedule,
                            contentPadding = PaddingValues(),
                            onReleaseClick = { onNavigate(it.id) },
                        )
                    }
                }
            }

            Indicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(innerPadding.only(WindowInsetsSides.Top)),
                isRefreshing = isRefreshing,
                state = pullToRefreshState
            )
        }
    }
}

@Composable
private fun TitleList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Release>,
    recommendedList: List<Release>,
    scheduleList: Map<DayOfWeek, List<Release>>,
    contentPadding: PaddingValues,
    onReleaseClick: (Release) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val pagerState = rememberPagerState(pageCount = { recommendedList.size })

    ProvideShimmer(shimmer) {
        LazyVerticalGrid(
            modifier = modifier.shimmerUpdater(shimmer),
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding
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
    contentPadding: PaddingValues,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    ProvideShimmer(shimmer) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState(), enabled = false)
                .padding(contentPadding),
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
