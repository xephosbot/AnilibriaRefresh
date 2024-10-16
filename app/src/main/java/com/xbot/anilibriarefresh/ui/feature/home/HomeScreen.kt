package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
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
import com.xbot.anilibriarefresh.ui.components.HeaderComponent
import com.xbot.anilibriarefresh.ui.components.LocalShimmer
import com.xbot.anilibriarefresh.ui.components.TitleCardItem
import com.xbot.anilibriarefresh.ui.components.TitleListItem
import com.xbot.anilibriarefresh.ui.components.TitlePagerItem
import com.xbot.anilibriarefresh.ui.utils.union
import com.xbot.domain.model.PosterModel
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

@OptIn(ExperimentalMaterialApi::class)
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
                targetState = loadStates.refresh,
                label = "" //TODO: информативный label для перехода
            ) { state ->
                when (state) {
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
    contentPadding: PaddingValues,
    onTitleClick: (Int) -> Unit
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val pagerState = rememberPagerState(pageCount = { listAnime.size }) //TODO: update pager state

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
                items = listAnime,
                state = pagerState
            ) { title ->
                //TODO: Pager item element
                TitlePagerItem(title = title)
            }
            header(
                title = "Избранное",
                onClick = {} //TODO: On click action
            )
            horizontalItems(
                contentPadding = PaddingValues(horizontal = 16.dp),
                items = listAnime
            ) { title ->
                TitleCardItem(title = title)
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

    CompositionLocalProvider(LocalShimmer provides shimmer) {
        Column(
            modifier = modifier
                .fillMaxSize()
                //TODO: переделать
                .padding(top = contentPadding.calculateTopPadding())
                .verticalScroll(rememberScrollState(), enabled = false),
        ) {
            TitlePagerItem(title = null)
            HeaderComponent("Избранное") { }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .horizontalScroll(rememberScrollState(), enabled = false)
            ) {
                repeat(4) {
                    TitleCardItem(title = null)
                }
            }
            HeaderComponent("Обновления") { }
            //TODO: добавить в загрузочный placeholder все элементы как на главном экране
            repeat(5) {
                TitleListItem(title = null)
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

private fun LazyListScope.header(
    title: String,
    onClick: () -> Unit
) {
    item(
        contentType = { "Header" }
    ) {
        HeaderComponent(title = title, onClick = onClick)
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

val listAnime = listOf(TitleModel(
    id = 1,
    name = "Клинок, рассекающий демонов",
    description = "Аниме об уничтожении мира, где главный герой может уничтожить весь мир и не хочет чтобы его друзья погибали",
    tags = listOf("2024", "TV", "Приключения"),
    poster = PosterModel(
        src = "/storage/releases/posters/8325/nCaLGeaSAbDMzqOOppaCuEoq60DnZCVf.jpg",
        thumbnail = null
    ),
    uploadedTime = null
),
    TitleModel(
        id = 2,
        name = "Атака титанов",
        description = "Аниме об уничтожении мира, где главный герой может уничтожить весь мир и не хочет чтобы его друзья погибали",
        tags = listOf("2023", "TV", "Экшен"),
        poster = PosterModel(
            src = "/storage/releases/posters/7439/QdCyM3mdXsUIfXtR.jpg",
            thumbnail = null
        ),
        uploadedTime = null
    ),
    TitleModel(
        id = 3,
        name = "Атака титанов",
        description = "Аниме об уничтожении мира, где главный герой может уничтожить весь мир и не хочет чтобы его друзья погибали",
        tags = listOf("2024", "TV", "Романтика"),
        poster = PosterModel(
            src = "/storage/releases/posters/7439/QdCyM3mdXsUIfXtR.jpg",
            thumbnail = null
        ),
        uploadedTime = null
    ),
    TitleModel(
        id = 4,
        name = "Атака титанов",
        description = "Аниме об уничтожении мира, где главный герой может уничтожить весь мир и не хочет чтобы его друзья погибали",
        tags = listOf("2024", "TV", "Приключения"),
        poster = PosterModel(
            src = "/storage/releases/posters/7439/QdCyM3mdXsUIfXtR.jpg",
            thumbnail = null
        ),
        uploadedTime = null
    ),
    TitleModel(
        id = 5,
        name = "Атака титанов",
        description = "Аниме об уничтожении мира, где главный герой может уничтожить весь мир и не хочет чтобы его друзья погибали",
        tags = listOf("2024", "TV", "Приключения"),
        poster = PosterModel(
            src = "/storage/releases/posters/7439/QdCyM3mdXsUIfXtR.jpg",
            thumbnail = null
        ),
        uploadedTime = null
    ),
    TitleModel(
        id = 6,
        name = "Атака титанов",
        description = "Аниме об уничтожении мира, где главный герой может уничтожить весь мир и не хочет чтобы его друзья погибали",
        tags = listOf("2024", "TV", "Приключения"),
        poster = PosterModel(
            src = "/storage/releases/posters/7439/QdCyM3mdXsUIfXtR.jpg",
            thumbnail = null
        ),
        uploadedTime = null
    ),
    TitleModel(
        id = 7,
        name = "Атака титанов",
        description = "Аниме об уничтожении мира, где главный герой может уничтожить весь мир и не хочет чтобы его друзья погибали",
        tags = listOf("2024", "TV", "Приключения"),
        poster = PosterModel(
            src = "/storage/releases/posters/7439/QdCyM3mdXsUIfXtR.jpg",
            thumbnail = null
        ),
        uploadedTime = null
    ))
