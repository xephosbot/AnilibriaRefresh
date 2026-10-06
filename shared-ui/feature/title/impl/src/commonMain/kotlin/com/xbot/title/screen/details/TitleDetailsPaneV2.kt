package com.xbot.title.screen.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.domain.models.ReleaseDetails
import com.xbot.title.screen.details2.CollectionStatusGroup
import com.xbot.title.screen.details2.ReleaseTabs
import com.xbot.title.screen.details2.StatusBanner
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
internal fun TitleDetailsPaneV2Content(
    state: TitleDetailsStateV2,
    onAction: (TitleDetailsActionV2) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val details = state.releaseDetails
    val shimmer = rememberShimmer(ShimmerBounds.Window)
    ProvideShimmer(shimmer) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            if (details == null) {
                LoadingContent()
                DetailsTopBar(
                    title = null,
                    solid = false,
                    onBackClick = onBackClick,
                    onShareClick = null,
                    onMoreClick = null
                )
            } else {
                LoadedContent(
                    state = state,
                    details = details,
                    onAction = onAction,
                    onBackClick = onBackClick
                )
            }
        }
    }
}

@Composable
private fun LoadedContent(
    state: TitleDetailsStateV2,
    details: ReleaseDetails,
    onAction: (TitleDetailsActionV2) -> Unit,
    onBackClick: () -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val tabs = ReleaseTab.entries
    val pagerState =
        rememberPagerState(initialPage = state.tab.ordinal) {
            tabs.size
        }
    val listStates = List(tabs.size) { rememberLazyListState() }

    var headerHeightPx by remember { mutableIntStateOf(0) }
    var tabsHeightPx by remember { mutableIntStateOf(0) }
    var titleBottomPx by remember { mutableIntStateOf(0) }
    var playBottomPx by remember { mutableIntStateOf(0) }
    var showMenu by rememberSaveable { mutableStateOf(false) }

    val pinnedPx =
        WindowInsets.statusBars.getTop(density) + with(density) { TopBarHeight.roundToPx() }
    val collapsiblePx = (headerHeightPx - tabsHeightPx - pinnedPx).coerceAtLeast(0)
    val activeList = listStates[pagerState.currentPage]
    val collapse = remember(activeList, collapsiblePx) {
        derivedStateOf {
            if (activeList.firstVisibleItemIndex == 0) {
                activeList.firstVisibleItemScrollOffset.coerceAtMost(collapsiblePx)
            } else {
                collapsiblePx
            }
        }
    }
    val topBarSolid by remember(pinnedPx) {
        derivedStateOf { titleBottomPx > 0 && titleBottomPx - collapse.value <= pinnedPx }
    }
    val fabVisible by remember(pinnedPx, state.isBlocked) {
        derivedStateOf {
            !state.isBlocked && playBottomPx > 0 &&
                playBottomPx - collapse.value <= pinnedPx
        }
    }
    val selectPage: (Int) -> Unit = { page ->
        scope.launch {
            listStates[page].syncWithHeader(collapse.value, collapsiblePx)
            pagerState.animateScrollToPage(page)
        }
    }

    PagerSyncEffects(
        pagerState = pagerState,
        listStates = listStates,
        selectedTab = state.tab,
        collapse = collapse,
        collapsiblePx = collapsiblePx,
        onTabSelect = { tab -> onAction(TitleDetailsActionV2.OnTabSelect(tab)) }
    )

    if (headerHeightPx > 0) {
        ReleasePages(
            modifier = Modifier.padding(top = with(density) { (pinnedPx + tabsHeightPx).toDp() }),
            state = state,
            details = details,
            pagerState = pagerState,
            listStates = listStates,
            headerSpacerHeight = with(density) { collapsiblePx.toDp() },
            bottomPadding =
                with(density) { WindowInsets.navigationBars.getBottom(density).toDp() } + FabSpace,
            onAction = onAction
        )
    }

    val currentList by rememberUpdatedState(activeList)
    ReleaseHeader(
        modifier = Modifier
            .onSizeChanged { headerHeightPx = it.height }
            .graphicsLayer { translationY = -collapse.value.toFloat() }
            .scrollable(
                state = rememberScrollableState { delta -> -currentList.dispatchRawDelta(-delta) },
                orientation = Orientation.Vertical
            ),
        state = state,
        details = details,
        pagerState = pagerState,
        onAction = onAction,
        onTabClick = selectPage,
        onTitleBottomChange = { titleBottomPx = it },
        onPlayBottomChange = { playBottomPx = it },
        onTabsHeightChange = { tabsHeightPx = it }
    )

    DetailsTopBar(
        title = details.release.name,
        solid = topBarSolid,
        onBackClick = onBackClick,
        onShareClick = { onAction(TitleDetailsActionV2.OnShareClick) },
        onMoreClick = { showMenu = true }
    )

    ReleaseFab(
        visible = fabVisible,
        currentEpisode = state.currentEpisode,
        onClick = { onAction(TitleDetailsActionV2.OnPlayClick) }
    )

    if (showMenu) {
        ReleaseActionsSheet(
            details = details,
            onDismiss = { showMenu = false },
            onAction = { action ->
                showMenu = false
                onAction(action)
            }
        )
    }
}

@Composable
private fun PagerSyncEffects(
    pagerState: PagerState,
    listStates: List<LazyListState>,
    selectedTab: ReleaseTab,
    collapse: State<Int>,
    collapsiblePx: Int,
    onTabSelect: (ReleaseTab) -> Unit
) {
    LaunchedEffect(selectedTab) {
        if (pagerState.currentPage != selectedTab.ordinal && !pagerState.isScrollInProgress) {
            pagerState.scrollToPage(selectedTab.ordinal)
        }
    }
    LaunchedEffect(pagerState, collapsiblePx) {
        snapshotFlow { pagerState.isScrollInProgress }
            .filter { it }
            .collect {
                listStates.forEachIndexed { index, listState ->
                    if (index != pagerState.currentPage) {
                        listState.syncWithHeader(collapse.value, collapsiblePx)
                    }
                }
            }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            onTabSelect(ReleaseTab.entries[page])
        }
    }
}

@Composable
private fun ReleasePages(
    state: TitleDetailsStateV2,
    details: ReleaseDetails,
    pagerState: PagerState,
    listStates: List<LazyListState>,
    headerSpacerHeight: Dp,
    bottomPadding: Dp,
    onAction: (TitleDetailsActionV2) -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        key = { page -> ReleaseTab.entries[page] }
    ) { page ->
        when (ReleaseTab.entries[page]) {
            ReleaseTab.Episodes -> EpisodesPage(
                state = state,
                listState = listStates[page],
                headerSpacerHeight = headerSpacerHeight,
                bottomPadding = bottomPadding,
                onAction = onAction
            )

            ReleaseTab.About -> AboutPage(
                state = state,
                details = details,
                listState = listStates[page],
                headerSpacerHeight = headerSpacerHeight,
                bottomPadding = bottomPadding,
                onAction = onAction
            )

            ReleaseTab.Ratings -> RatingsPage(
                details = details,
                isActive = pagerState.settledPage == page,
                listState = listStates[page],
                headerSpacerHeight = headerSpacerHeight,
                bottomPadding = bottomPadding,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun ReleaseHeader(
    state: TitleDetailsStateV2,
    details: ReleaseDetails,
    pagerState: PagerState,
    onAction: (TitleDetailsActionV2) -> Unit,
    onTabClick: (Int) -> Unit,
    onTitleBottomChange: (Int) -> Unit,
    onPlayBottomChange: (Int) -> Unit,
    onTabsHeightChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var headerCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { headerCoordinates = it }
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        ReleaseHero(poster = details.release.poster)
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            ReleaseMetaChips(
                modifier = Modifier.padding(top = 4.dp),
                details = details
            )
            ReleaseTitleBlock(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .onGloballyPositioned { onTitleBottomChange(it.bottomIn(headerCoordinates)) },
                details = details
            )
            StatusBanner(
                modifier = Modifier.padding(top = 16.dp),
                banner = state.statusBanner,
                onExternalPlayerClick = { onAction(TitleDetailsActionV2.OnExternalPlayerClick) }
            )
            PrimaryActions(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .onGloballyPositioned { onPlayBottomChange(it.bottomIn(headerCoordinates)) },
                playButton = state.playButton,
                isBlocked = state.isBlocked,
                isFavorite = state.isFavorite,
                favoritesCount = details.release.favoritesCount,
                onPlayClick = { onAction(TitleDetailsActionV2.OnPlayClick) },
                onFavoriteToggle = { onAction(TitleDetailsActionV2.OnFavoriteToggle) }
            )
        }
        CollectionStatusGroup(
            modifier = Modifier.padding(top = 10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            selected = state.collectionStatus,
            onSelect = { status ->
                onAction(TitleDetailsActionV2.OnCollectionStatusSelect(status))
            }
        )
        Spacer(Modifier.height(16.dp))
        ReleaseTabs(
            modifier = Modifier.onSizeChanged { onTabsHeightChange(it.height) },
            selectedTab = ReleaseTab.entries[pagerState.currentPage],
            episodesCount = details.episodes.size,
            onTabClick = { tab -> onTabClick(tab.ordinal) }
        )
    }
}

private fun LayoutCoordinates.bottomIn(ancestor: LayoutCoordinates?): Int {
    if (ancestor == null || !ancestor.isAttached || !isAttached) return 0
    return (ancestor.localPositionOf(this, Offset.Zero).y + size.height).roundToInt()
}

private suspend fun LazyListState.syncWithHeader(collapse: Int, collapsible: Int) {
    if (collapse < collapsible) {
        scrollToItem(0, collapse)
    } else if (firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset < collapsible) {
        scrollToItem(0, collapsible)
    }
}

internal val TopBarHeight = 64.dp
private val FabSpace = 96.dp
internal const val HEADER_SPACER_KEY = "header-spacer"

@AnilibertyPreview
@Composable
private fun TitleDetailsPaneV2Preview(
    @PreviewParameter(TitleDetailsStateV2Provider::class) state: TitleDetailsStateV2
) {
    TitleDetailsPaneV2Content(
        state = state,
        onAction = {},
        onBackClick = {}
    )
}

private class TitleDetailsStateV2Provider : PreviewParameterProvider<TitleDetailsStateV2> {
    override val values = sequenceOf(
        TitleDetailsPreviewDataV2.ongoing,
        TitleDetailsPreviewDataV2.finished,
        TitleDetailsPreviewDataV2.geoBlocked,
        TitleDetailsPreviewDataV2.manyEpisodes,
        TitleDetailsPreviewDataV2.newViewer,
        TitleDetailsPreviewDataV2.withoutFranchise,
        TitleDetailsPreviewDataV2.ratings,
        TitleDetailsPreviewDataV2.loading
    )
}
