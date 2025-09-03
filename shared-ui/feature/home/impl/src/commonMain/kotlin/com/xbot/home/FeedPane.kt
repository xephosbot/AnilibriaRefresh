package com.xbot.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.State
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.GenreItem
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.ReleaseCardItem
import com.xbot.designsystem.components.ReleaseLargeCard
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.horizontalPagerItems
import com.xbot.designsystem.components.pagingItems
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.AnilibriaLogoLarge
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.fadeWithParallax
import com.xbot.designsystem.modifier.horizontalParallax
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.modifier.verticalParallax
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.localization.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.button_watch
import com.xbot.resources.label_genres
import com.xbot.resources.label_schedule
import com.xbot.resources.label_updates
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
internal fun ThreePaneScaffoldPaneScope.FeedPane(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    items: LazyPagingItems<Release>,
    onSearchClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    onShowErrorMessage: (Throwable) -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    var topAppbarHeight by remember { mutableStateOf(0) }
    val topAppBarAlpha by remember {
        derivedStateOf {
            val firstItem = gridState.layoutInfo.visibleItemsInfo.firstOrNull()
            if (firstItem == null) {
                0f
            } else if (gridState.firstVisibleItemIndex > 0) {
                1f
            } else {
                val scrollProgress =
                    (firstItem.offset.y.absoluteValue / (firstItem.size.height - topAppbarHeight).toFloat()).coerceIn(0f, 1f)
                if (scrollProgress < 0.5f) {
                    0f
                } else {
                    ((scrollProgress - 0.5f) * 2f).coerceIn(0f, 1f)
                }
            }
        }
    }

    LaunchedEffect(items) {
        snapshotFlow { items.loadState }.collect { loadState ->
            (loadState.refresh as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
            (loadState.append as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
            (loadState.prepend as? LoadState.Error)?.let { onShowErrorMessage(it.error) }
        }
    }

    val isRefreshing by remember(state, items) {
        derivedStateOf { items.loadState.refresh == LoadState.Loading || state is HomeScreenState.Loading }
    }

    val showResetScrollButton by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0
        }
    }

    AnimatedPane(modifier = modifier) {
        Scaffold(
            modifier = Modifier.pullToRefresh(
                isRefreshing = isRefreshing,
                state = pullToRefreshState,
                onRefresh = onRefresh
            ),
            topBar = {
                TopAppBar(
                    title = {
                        Image(
                            imageVector = AnilibriaIcons.Filled.AnilibriaLogoLarge,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            contentDescription = null
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = onSearchClick,
                            shapes = IconButtonDefaults.shapes()
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.Outlined.Search,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = topAppBarAlpha),
                    )
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = gridState.isScrollingUp().value && showResetScrollButton,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    FilledTonalIconButton(
                        onClick = {
                            scope.launch {
                                gridState.animateScrollToItem(0)
                            }
                        },
                        shapes = IconButtonDefaults.shapes()
                    ) {
                        Icon(
                            modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
                            imageVector = AnilibriaIcons.Outlined.ArrowDropUp,
                            contentDescription = null
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) { innerPadding ->
            topAppbarHeight = with(LocalDensity.current) { innerPadding.calculateTopPadding().roundToPx() }

            Box {
                Crossfade(
                    targetState = state,
                    label = "Loading state transition in HomeScreenContent",
                ) { targetState ->
                    when (targetState) {
                        is HomeScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                        is HomeScreenState.Success -> {
                            ReleaseFeed(
                                gridState = gridState,
                                items = items,
                                recommendedReleases = targetState.releasesFeed.recommendedReleases,
                                genres = targetState.releasesFeed.genres,
                                scheduleList = targetState.releasesFeed.schedule,
                                contentPadding = innerPadding,
                                onScheduleClick = onScheduleClick,
                                onReleaseClick = { onReleaseClick(it.id) },
                            )
                        }
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ReleaseFeed(
    modifier: Modifier = Modifier,
    gridState: LazyGridState,
    items: LazyPagingItems<Release>,
    recommendedReleases: List<Release>,
    genres: List<Genre>,
    scheduleList: Map<DayOfWeek, List<Release>>,
    contentPadding: PaddingValues,
    onScheduleClick: () -> Unit,
    onReleaseClick: (Release) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val pagerState = rememberPagerState(pageCount = { recommendedReleases.size })

    ProvideShimmer(shimmer) {
        Feed(
            modifier = modifier.shimmerUpdater(shimmer),
            state = gridState,
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        ) {
            horizontalPagerItems(
                items = recommendedReleases,
                state = pagerState
            ) { page, release ->
                ReleaseLargeCard(
                    modifier = Modifier
                        .horizontalParallax(pagerState, page)
                        .verticalParallax(gridState),
                    contentModifier = Modifier
                        .fadeWithParallax(pagerState, page),
                    release = release
                ) {
                    val releaseCardInteractionSources = remember { List(2) { MutableInteractionSource() } }
                    var checked by remember { mutableStateOf(false) }

                    ButtonGroup {
                        Button(
                            modifier = Modifier
                                .heightIn(ButtonDefaults.MediumContainerHeight)
                                .animateWidth(releaseCardInteractionSources[0]),
                            onClick = { onReleaseClick(release) },
                            shapes = ButtonDefaults.shapes(),
                            contentPadding = ButtonDefaults.MediumContentPadding,
                            interactionSource = releaseCardInteractionSources[0]
                        ) {
                            Icon(
                                modifier = Modifier.size(ButtonDefaults.MediumIconSize),
                                imageVector = AnilibriaIcons.Filled.PlayArrow,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(ButtonDefaults.MediumIconSpacing))
                            Text(
                                text = stringResource(Res.string.button_watch),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }

                        FilledTonalIconToggleButton(
                            checked = checked,
                            onCheckedChange = { checked = it },
                            modifier = Modifier
                                .size(IconButtonDefaults.mediumContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide))
                                .animateWidth(releaseCardInteractionSources[1]),
                            shapes = IconButtonDefaults.toggleableShapes(),
                            colors = IconButtonDefaults.filledTonalIconToggleButtonColors(
                                checkedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                                checkedContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.inverseSurface),
                            ),
                            interactionSource = releaseCardInteractionSources[1],
                        ) {
                            Icon(
                                modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
                                imageVector = AnilibriaIcons.Outlined.Star,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            header(
                title = { Text(text = stringResource(Res.string.label_schedule)) },
                onClick = onScheduleClick
            )
            horizontalItems(
                items = scheduleList,
                contentPadding = PaddingValues(horizontal = 16.dp),
                stickyHeader = { dayOfWeek ->
                    Column {
                        Text(
                            text = dayOfWeek.toLocalizedString(),
                            style = MaterialTheme.typography.labelMedium
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

            header(
                title = { Text(text = stringResource(Res.string.label_genres)) }
            )
            horizontalItems(
                items = genres,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) { genre ->
                GenreItem(
                    genre = genre,
                    onClick = { /*TODO*/ }
                )
            }

            header(
                title = { Text(text = stringResource(Res.string.label_updates)) },
            )
            pagingItems(items) { index, release ->
                Column {
                    ReleaseListItem(
                        modifier = Modifier.feedItemSpacing(index),
                        release = release,
                        onClick = onReleaseClick,
                    )
                    if (index < items.itemCount - 1) {
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)
    val textHeight = with(LocalDensity.current) {
        MaterialTheme.typography.labelMedium.fontSize.toDp()
    }

    ProvideShimmer(shimmer) {
        Column(
            modifier = modifier
                .padding(contentPadding.only(WindowInsetsSides.Horizontal))
                .verticalScroll(rememberScrollState(), enabled = false)
        ) {
            ReleaseLargeCard(null) {
                ButtonGroup {
                    Button(
                        modifier = Modifier.heightIn(ButtonDefaults.MediumContainerHeight),
                        onClick = { },
                        shapes = ButtonDefaults.shapes(),
                        contentPadding = ButtonDefaults.MediumContentPadding,
                    ) {
                        Icon(
                            modifier = Modifier.size(ButtonDefaults.MediumIconSize),
                            imageVector = AnilibriaIcons.Filled.PlayArrow,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(ButtonDefaults.MediumIconSpacing))
                        Text(
                            text = stringResource(Res.string.button_watch),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    FilledTonalIconButton(
                        modifier = Modifier
                            .size(IconButtonDefaults.mediumContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
                        onClick = {},
                        shapes = IconButtonDefaults.shapes(),
                    ) {
                        Icon(
                            modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
                            imageVector = AnilibriaIcons.Outlined.Star,
                            contentDescription = null
                        )
                    }
                }
            }
            Header(
                title = { Text(stringResource(Res.string.label_schedule)) },
                onClick = {},
            )
            Spacer(Modifier.height(textHeight))
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState(), enabled = false)
                    .padding(horizontal = 16.dp)
            ) {
                repeat(10) {
                    ReleaseCardItem(release = null) {}
                }
            }
        }
    }
}

@Composable
private fun LazyGridState.isScrollingUp(): State<Boolean> {
    return produceState(initialValue = true) {
        var lastIndex = 0
        var lastScroll = Int.MAX_VALUE
        snapshotFlow {
            firstVisibleItemIndex to firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentScroll) ->
            if (currentIndex != lastIndex || currentScroll != lastScroll) {
                value = currentIndex < lastIndex ||
                        (currentIndex == lastIndex && currentScroll < lastScroll)
                lastIndex = currentIndex
                lastScroll = currentScroll
            }
        }
    }
}