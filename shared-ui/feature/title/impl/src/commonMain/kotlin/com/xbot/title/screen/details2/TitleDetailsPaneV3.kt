package com.xbot.title.screen.details2

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ConnectedButtonGroupDefaults
import com.xbot.designsystem.components.LargeReleaseCard
import com.xbot.designsystem.components.SingleChoiceConnectedButtonGroup
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.Copyright
import com.xbot.designsystem.icons.MoreVert
import com.xbot.designsystem.icons.OpenInNew
import com.xbot.designsystem.icons.PublicOff
import com.xbot.designsystem.icons.Star
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.modifier.verticalParallax
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.enums.CollectionType
import com.xbot.resources.Res
import com.xbot.resources.release_details_open_external_player
import com.xbot.resources.release_details_tab_about
import com.xbot.resources.release_details_tab_episodes
import com.xbot.resources.release_details_tab_ratings
import com.xbot.resources.stringResource
import com.xbot.title.component.FavoriteButton
import com.xbot.title.component.PlayButton
import com.xbot.title.screen.details.AboutPage
import com.xbot.title.component.AlertCard
import com.xbot.title.screen.details.CollectionStatusOrder
import com.xbot.title.screen.details.EpisodesPage
import com.xbot.title.component.NotificationCard
import com.xbot.title.screen.details.PlayButtonState
import com.xbot.title.screen.details.RatingsPage
import com.xbot.title.screen.details.ReleaseStatusBanner
import com.xbot.title.screen.details.ReleaseTab
import com.xbot.title.screen.details.TitleDetailsActionV2
import com.xbot.title.screen.details.TitleDetailsPreviewDataV2
import com.xbot.title.screen.details.TitleDetailsStateV2
import com.xbot.title.screen.details.icon
import com.xbot.title.screen.details.labelRes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TitleDetailsPaneContentV3(
    state: TitleDetailsStateV2,
    modifier: Modifier = Modifier,
    onAction: (TitleDetailsActionV2) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    ProvideShimmer(shimmer) {
        Scaffold(
            modifier = modifier
                .shimmerUpdater(shimmer),
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surfaceContainer,
                                    MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0f)
                                )
                            )
                        ),
                    title = {},
                    navigationIcon = {
                        FilledIconButton(
                            onClick = onBackClick,
                            shapes = IconButtonDefaults.shapes()
                        ) {
                            Icon(
                                imageVector = AnilibertyIcons.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        FilledIconButton(
                            onClick = {},
                            shapes = IconButtonDefaults.shapes()
                        ) {
                            Icon(
                                imageVector = AnilibertyIcons.Filled.Star,
                                contentDescription = null
                            )
                        }
                        FilledIconButton(
                            onClick = {},
                            modifier = Modifier.size(
                                IconButtonDefaults.smallContainerSize(
                                    IconButtonDefaults.IconButtonWidthOption.Narrow
                                )
                            ),
                            shapes = IconButtonDefaults.shapes()
                        ) {
                            Icon(
                                imageVector = AnilibertyIcons.MoreVert,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) { innerPadding ->
            TitleDetails(
                state = state,
                contentPadding = innerPadding,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun TitleDetails(
    state: TitleDetailsStateV2,
    contentPadding: PaddingValues,
    onAction: (TitleDetailsActionV2) -> Unit
) {
    val listState = rememberLazyListState()
    val pagerState = rememberPagerState(initialPage = state.tab.ordinal) { ReleaseTab.entries.size }
    val listStates = List(ReleaseTab.entries.size) { rememberLazyListState() }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val currentOnAction by rememberUpdatedState(onAction)
    var tabsHeight by remember { mutableStateOf(0.dp) }
    val horizontalMargin = 16.dp
    val headerFirstConnection = remember(listState) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
                if (available.y < 0f) {
                    Offset(0f, -listState.dispatchRawDelta(-available.y))
                } else {
                    Offset.Zero
                }
        }
    }

    LaunchedEffect(state.tab) {
        if (pagerState.currentPage != state.tab.ordinal && !pagerState.isScrollInProgress) {
            pagerState.scrollToPage(state.tab.ordinal)
        }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            currentOnAction(TitleDetailsActionV2.OnTabSelect(ReleaseTab.entries[page]))
        }
    }

    BoxWithConstraints {
        val pagerHeight =
            (maxHeight - contentPadding.calculateTopPadding() - tabsHeight).coerceAtLeast(0.dp)

        LazyColumn(state = listState) {
            item(key = "card", contentType = "card") {
                LargeReleaseCard(
                    modifier = Modifier.verticalParallax(listState),
                    contentModifier = Modifier.animateContentSize(),
                    release = state.release,
                    contentPadding = PaddingValues(horizontal = horizontalMargin) +
                        contentPadding.only(WindowInsetsSides.Horizontal)
                ) { contentAlignment ->
                    state.releaseDetails?.alternativeName?.let { alternativeName ->
                        Text(
                            text = alternativeName.lines().joinToString(" "),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = if (contentAlignment == Alignment.Start) {
                                TextAlign.Start
                            } else {
                                TextAlign.Center
                            },
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    PrimaryActions(
                        playButton = state.playButton,
                        isBlocked = state.isBlocked,
                        isFavorite = state.isFavorite,
                        favoritesCount = state.release?.favoritesCount ?: 0,
                        onPlayClick = { onAction(TitleDetailsActionV2.OnPlayClick) },
                        onFavoriteToggle = { onAction(TitleDetailsActionV2.OnFavoriteToggle) }
                    )
                }
            }

            item(key = "collection_status", contentType = "collection_status") {
                CollectionStatusGroup(
                    modifier = Modifier.padding(top = 10.dp),
                    selected = state.collectionStatus,
                    onSelect = { status ->
                        onAction(TitleDetailsActionV2.OnCollectionStatusSelect(status))
                    },
                    contentPadding = PaddingValues(horizontal = horizontalMargin)
                )
            }

            item(key = "status_banner", contentType = "status_banner") {
                StatusBanner(
                    modifier = Modifier
                        .padding(horizontal = horizontalMargin)
                        .padding(top = 10.dp),
                    banner = state.statusBanner,
                    onExternalPlayerClick = {
                        onAction(TitleDetailsActionV2.OnExternalPlayerClick)
                    }
                )
            }

            stickyHeader(key = "tabs", contentType = "tabs") {
                ReleaseTabs(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .onSizeChanged {
                            tabsHeight = with(density) { it.height.toDp() }
                        },
                    selectedTab = ReleaseTab.entries[pagerState.currentPage],
                    episodesCount = state.releaseDetails?.episodes?.size ?: 0,
                    onTabClick = { tab ->
                        scope.launch {
                            pagerState.animateScrollToPage(tab.ordinal)
                        }
                    }
                )
            }

            item(key = "pager", contentType = "pager") {
                ReleasePager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(pagerHeight)
                        .nestedScroll(headerFirstConnection),
                    state = state,
                    pagerState = pagerState,
                    listStates = listStates,
                    bottomPadding = contentPadding.calculateBottomPadding(),
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
internal fun PrimaryActions(
    playButton: PlayButtonState,
    isBlocked: Boolean,
    isFavorite: Boolean,
    favoritesCount: Int,
    onPlayClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlayButton(
            modifier = Modifier.weight(1f),
            title = stringResource(playButton.title),
            subtitle = stringResource(playButton.subtitle),
            progress = playButton.progress,
            enabled = !isBlocked,
            onClick = onPlayClick
        )
        FavoriteButton(
            modifier = Modifier.fillMaxHeight(),
            checked = isFavorite,
            count = favoritesCount,
            onCheckedChange = { onFavoriteToggle() }
        )
    }
}

@Composable
private fun ReleasePager(
    state: TitleDetailsStateV2,
    pagerState: PagerState,
    listStates: List<LazyListState>,
    bottomPadding: Dp,
    onAction: (TitleDetailsActionV2) -> Unit,
    modifier: Modifier = Modifier
) {
    val details = state.releaseDetails
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        key = { page -> ReleaseTab.entries[page] }
    ) { page ->
        when (ReleaseTab.entries[page]) {
            ReleaseTab.Episodes -> EpisodesPage(
                state = state,
                listState = listStates[page],
                headerSpacerHeight = 0.dp,
                bottomPadding = bottomPadding,
                onAction = onAction
            )

            ReleaseTab.About -> if (details != null) {
                AboutPage(
                    state = state,
                    details = details,
                    listState = listStates[page],
                    headerSpacerHeight = 0.dp,
                    bottomPadding = bottomPadding,
                    onAction = onAction
                )
            }

            ReleaseTab.Ratings -> if (details != null) {
                RatingsPage(
                    details = details,
                    isActive = pagerState.settledPage == page,
                    listState = listStates[page],
                    headerSpacerHeight = 0.dp,
                    bottomPadding = bottomPadding,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
internal fun StatusBanner(
    banner: ReleaseStatusBanner,
    onExternalPlayerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (banner) {
        ReleaseStatusBanner.None -> Unit

        is ReleaseStatusBanner.Blocked -> AlertCard(
            modifier = modifier,
            icon = when (banner) {
                is ReleaseStatusBanner.GeoBlocked -> AnilibertyIcons.PublicOff
                is ReleaseStatusBanner.CopyrightBlocked -> AnilibertyIcons.Copyright
            },
            title = stringResource(banner.title),
            text = banner.description?.let { stringResource(it) },
            action = if (banner.hasExternalPlayer) {
                {
                    Button(
                        onClick = onExternalPlayerClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onErrorContainer,
                            contentColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            imageVector = AnilibertyIcons.OpenInNew,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.padding(start = ButtonDefaults.IconSpacing),
                            text = stringResource(Res.string.release_details_open_external_player)
                        )
                    }
                }
            } else {
                null
            }
        )

        is ReleaseStatusBanner.Notification -> NotificationCard(
            modifier = modifier,
            text = stringResource(banner.text)
        )
    }
}

@Composable
internal fun ReleaseTabs(
    selectedTab: ReleaseTab,
    episodesCount: Int,
    onTabClick: (ReleaseTab) -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryTabRow(
        modifier = modifier,
        selectedTabIndex = selectedTab.ordinal,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    selectedTab.ordinal,
                    matchContentSize = true
                )
            )
        }
    ) {
        ReleaseTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            Tab(
                selected = selected,
                onClick = { onTabClick(tab) },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(tab.titleRes),
                            style = MaterialTheme.typography.labelLarge
                        )
                        if (tab == ReleaseTab.Episodes && episodesCount > 0) {
                            Badge(
                                containerColor = if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainerHigh
                                },
                                contentColor = if (selected) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            ) {
                                Text(text = episodesCount.toString())
                            }
                        }
                    }
                }
            )
        }
    }
}

private val ReleaseTab.titleRes
    get() = when (this) {
        ReleaseTab.Episodes -> Res.string.release_details_tab_episodes
        ReleaseTab.About -> Res.string.release_details_tab_about
        ReleaseTab.Ratings -> Res.string.release_details_tab_ratings
    }

@Composable
internal fun CollectionStatusGroup(
    selected: CollectionType?,
    onSelect: (CollectionType?) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    SingleChoiceConnectedButtonGroup(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(contentPadding),
        items = CollectionStatusOrder,
        selectedItem = selected
    ) { checked, status ->
        FilledTonalToggleButton(
            checked = checked,
            onCheckedChange = { isChecked -> onSelect(if (isChecked) status else null) },
            shapes = ConnectedButtonGroupDefaults.connectedButtonShapes(
                index = CollectionStatusOrder.indexOf(status),
                count = CollectionStatusOrder.size
            )
        ) {
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                imageVector = status.icon,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = ButtonDefaults.IconSpacing),
                text = stringResource(status.labelRes)
            )
        }
    }
}

@AnilibertyPreview
@Composable
private fun TitleDetailsPanePreview() {
    TitleDetailsPaneContentV3(
        state = TitleDetailsPreviewDataV2.geoBlocked
    )
}
