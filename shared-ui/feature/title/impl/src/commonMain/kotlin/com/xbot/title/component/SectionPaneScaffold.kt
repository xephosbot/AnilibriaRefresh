package com.xbot.title.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculateThreePaneScaffoldValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.WindowInsetsRulers
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.theme.LocalMargins
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

internal enum class PaneSectionRole { Header, Main, Supporting, Extra }

@Immutable
internal data class PaneSection<K : Any>(
    val key: K,
    val role: PaneSectionRole,
    val title: String = ""
)

@Stable
internal interface PaneSectionScope {
    val listState: LazyListState
    val contentPadding: PaddingValues
    val isActive: Boolean
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun calculateSectionPaneScaffoldDirective(
    windowAdaptiveInfo: WindowAdaptiveInfo
): PaneScaffoldDirective {
    val directive = calculatePaneScaffoldDirective(windowAdaptiveInfo)
    return if (windowAdaptiveInfo.windowPosture.isTabletop) {
        directive.copy(
            maxHorizontalPartitions = 1,
            maxVerticalPartitions = 2
        )
    } else {
        directive.copy(
            maxVerticalPartitions = 1,
            verticalPartitionSpacerSize = 0.dp
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun <K : Any> SectionPaneScaffold(
    directive: PaneScaffoldDirective,
    sections: List<PaneSection<K>>,
    selectedSection: K,
    onSectionSelect: (K) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    content: @Composable PaneSectionScope.(section: K) -> Unit
) {
    val header = sections.firstOrNull { it.role == PaneSectionRole.Header }
    val tabSections = sections.filter { it.role != PaneSectionRole.Header }
    val scaffoldDirective = directive.fitTo(tabSections)
    val scaffoldValue = calculateThreePaneScaffoldValue(
        maxHorizontalPartitions = scaffoldDirective.maxHorizontalPartitions,
        adaptStrategies = SupportingPaneScaffoldDefaults.adaptStrategies(),
        destinationHistory = emptyList(),
        maxVerticalPartitions = scaffoldDirective.maxVerticalPartitions
    )
    val layout = scaffoldValue.resolve(tabSections)
    val isMultiPane = scaffoldValue.hasSideBySidePanes()
    val mainListState = rememberLazyListState()
    val context = SectionPaneContext(
        listStates = tabSections.associate { section ->
            section.key to key(section.key) { rememberLazyListState() }
        },
        selectedSection = selectedSection,
        onSectionSelect = onSectionSelect,
        content = content
    )
    val containerColor = MaterialTheme.colorScheme.surfaceContainer

    Column(
        modifier = modifier.background(
            if (isMultiPane) MaterialTheme.colorScheme.surface else containerColor
        )
    ) {
        if (isMultiPane) {
            topBar()
        }
        SupportingPaneScaffold(
            directive = scaffoldDirective,
            value = scaffoldValue,
            modifier = Modifier
                .weight(1f)
                .then(
                    if (isMultiPane) {
                        Modifier.consumeWindowInsets(WindowInsets.safeDrawing)
                    } else {
                        Modifier
                    }
                ),
            mainPane = {
                SectionAnimatedPane(isMultiPane = isMultiPane) {
                    MainPane(
                        header = header,
                        sections = layout.main,
                        listState = mainListState,
                        context = context,
                        topBar = {
                            if (!isMultiPane) {
                                Box(
                                    modifier = Modifier.background(
                                        Brush.verticalGradient(
                                            listOf(containerColor, containerColor.copy(alpha = 0f))
                                        )
                                    )
                                ) {
                                    topBar()
                                }
                            }
                        }
                    )
                }
            },
            supportingPane = {
                SectionAnimatedPane(
                    isMultiPane = isMultiPane,
                    modifier = Modifier.preferredHeight(0.5f)
                ) {
                    TabsPane(sections = layout.supporting, context = context)
                }
            },
            extraPane = {
                SectionAnimatedPane(isMultiPane = isMultiPane) {
                    TabsPane(sections = layout.extra, context = context)
                }
            }
        )
    }
}

@Composable
private fun <K : Any> PaneSectionTabRow(
    sections: List<PaneSection<K>>,
    selectedSection: K,
    onSectionClick: (K) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = sections.indexOfFirst { it.key == selectedSection }.coerceAtLeast(0)
    PrimaryTabRow(
        modifier = modifier,
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    selectedIndex,
                    matchContentSize = true
                )
            )
        }
    ) {
        sections.forEach { section ->
            Tab(
                selected = section.key == selectedSection,
                onClick = { onSectionClick(section.key) },
                text = {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}

private class SectionPaneContext<K : Any>(
    val listStates: Map<K, LazyListState>,
    val selectedSection: K,
    val onSectionSelect: (K) -> Unit,
    val content: @Composable PaneSectionScope.(section: K) -> Unit
)

@Composable
internal fun rememberPaneSectionScope(
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    isActive: Boolean = true
): PaneSectionScope = remember(listState, contentPadding, isActive) {
    PaneSectionScopeImpl(listState, contentPadding, isActive)
}

private class PaneSectionScopeImpl(
    override val listState: LazyListState,
    override val contentPadding: PaddingValues,
    override val isActive: Boolean
) : PaneSectionScope

private class SectionLayout<K : Any>(
    val main: List<PaneSection<K>>,
    val supporting: List<PaneSection<K>>,
    val extra: List<PaneSection<K>>
)

private fun PaneScaffoldDirective.fitTo(tabSections: List<PaneSection<*>>): PaneScaffoldDirective {
    val sidePaneCount = listOf(PaneSectionRole.Supporting, PaneSectionRole.Extra)
        .count { role -> tabSections.any { it.role == role } }
    return copy(
        maxHorizontalPartitions = minOf(maxHorizontalPartitions, 1 + sidePaneCount),
        maxVerticalPartitions = if (tabSections.isEmpty()) 1 else maxVerticalPartitions
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <K : Any> ThreePaneScaffoldValue.resolve(
    tabSections: List<PaneSection<K>>
): SectionLayout<K> {
    fun withRoles(vararg roles: PaneSectionRole) = tabSections.filter { it.role in roles }

    val isSupportingSideBySide = secondary == PaneAdaptedValue.Expanded
    val isExtraSideBySide = tertiary == PaneAdaptedValue.Expanded
    return when {
        isSupportingSideBySide && isExtraSideBySide -> SectionLayout(
            main = withRoles(PaneSectionRole.Main),
            supporting = withRoles(PaneSectionRole.Supporting),
            extra = withRoles(PaneSectionRole.Extra)
        )

        isSupportingSideBySide -> SectionLayout(
            main = withRoles(PaneSectionRole.Main),
            supporting = withRoles(PaneSectionRole.Supporting, PaneSectionRole.Extra),
            extra = emptyList()
        )

        secondary != PaneAdaptedValue.Hidden -> SectionLayout(
            main = emptyList(),
            supporting = tabSections,
            extra = emptyList()
        )

        else -> SectionLayout(
            main = tabSections,
            supporting = emptyList(),
            extra = emptyList()
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldValue.hasSideBySidePanes(): Boolean =
    listOf(primary, secondary, tertiary).count { it == PaneAdaptedValue.Expanded } > 1

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ThreePaneScaffoldPaneScope.SectionAnimatedPane(
    isMultiPane: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val containerModifier = if (isMultiPane) {
        val margin = LocalMargins.current.horizontal
        Modifier
            .paneMargins(
                PaddingValues(start = margin, end = margin, bottom = margin),
                WindowInsetsRulers.SafeDrawing.current
            )
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    } else {
        Modifier
    }
    AnimatedPane(
        modifier = modifier
            .preferredWidth(1f)
            .then(containerModifier)
    ) {
        content()
    }
}

@Composable
private fun <K : Any> MainPane(
    header: PaneSection<K>?,
    sections: List<PaneSection<K>>,
    listState: LazyListState,
    context: SectionPaneContext<K>,
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        val pagerState = if (sections.isNotEmpty()) {
            rememberSectionPagerState(sections, context.selectedSection, context.onSectionSelect)
        } else {
            null
        }
        val scope = rememberCoroutineScope()
        val density = LocalDensity.current
        var tabRowHeight by remember { mutableStateOf(0.dp) }
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

        BoxWithConstraints {
            val hasTabRow = sections.size > 1
            val pinnedHeight = if (hasTabRow) tabRowHeight else 0.dp
            val pagerHeight = (maxHeight - innerPadding.calculateTopPadding() - pinnedHeight)
                .coerceAtLeast(0.dp)

            LazyColumn(state = listState) {
                if (header != null) {
                    item(key = HEADER_KEY, contentType = HEADER_KEY) {
                        context.SectionContent(
                            section = header.key,
                            listState = listState,
                            contentPadding = innerPadding,
                            isActive = true
                        )
                    }
                }

                if (pagerState != null) {
                    if (hasTabRow) {
                        stickyHeader(key = TAB_ROW_KEY, contentType = TAB_ROW_KEY) {
                            PaneSectionTabRow(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .onSizeChanged {
                                        tabRowHeight = with(density) { it.height.toDp() }
                                    },
                                sections = sections,
                                selectedSection = sections[pagerState.currentPage].key,
                                onSectionClick = { clicked ->
                                    scope.launch {
                                        pagerState.animateScrollToPage(
                                            sections.indexOfFirst { it.key == clicked }
                                        )
                                    }
                                }
                            )
                        }
                    }

                    item(key = PAGER_KEY, contentType = PAGER_KEY) {
                        SectionPager(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(pagerHeight)
                                .nestedScroll(headerFirstConnection),
                            sections = sections,
                            pagerState = pagerState,
                            pageContentPadding = PaddingValues(
                                bottom = innerPadding.calculateBottomPadding()
                            ),
                            context = context
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun <K : Any> TabsPane(
    sections: List<PaneSection<K>>,
    context: SectionPaneContext<K>,
    modifier: Modifier = Modifier
) {
    if (sections.isEmpty()) return

    val pagerState = rememberSectionPagerState(
        sections,
        context.selectedSection,
        context.onSectionSelect
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .fitInside(WindowInsetsRulers.SafeDrawing.current)
    ) {
        if (sections.size > 1) {
            PaneSectionTabRow(
                sections = sections,
                selectedSection = sections[pagerState.currentPage].key,
                onSectionClick = { clicked ->
                    scope.launch {
                        pagerState.animateScrollToPage(sections.indexOfFirst { it.key == clicked })
                    }
                }
            )
        }
        SectionPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            sections = sections,
            pagerState = pagerState,
            pageContentPadding = PaddingValues(),
            context = context
        )
    }
}

@Composable
private fun <K : Any> SectionPager(
    sections: List<PaneSection<K>>,
    pagerState: PagerState,
    pageContentPadding: PaddingValues,
    context: SectionPaneContext<K>,
    modifier: Modifier = Modifier
) {
    key(pagerState) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
        ) { index ->
            val section = sections[index].key
            context.SectionContent(
                section = section,
                listState = context.listStates.getValue(section),
                contentPadding = pageContentPadding,
                isActive = pagerState.settledPage == index
            )
        }
    }
}

@Composable
private fun <K : Any> SectionPaneContext<K>.SectionContent(
    section: K,
    listState: LazyListState,
    contentPadding: PaddingValues,
    isActive: Boolean
) {
    content(PaneSectionScopeImpl(listState, contentPadding, isActive), section)
}

@Composable
private fun <K : Any> rememberSectionPagerState(
    sections: List<PaneSection<K>>,
    selectedSection: K,
    onSectionSelect: (K) -> Unit
): PagerState {
    val keys = sections.map { it.key }
    val pagerState = key(keys) {
        rememberPagerState(initialPage = keys.indexOf(selectedSection).coerceAtLeast(0)) {
            keys.size
        }
    }
    val currentOnSectionSelect by rememberUpdatedState(onSectionSelect)

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .drop(1)
            .collect { index -> currentOnSectionSelect(keys[index]) }
    }
    LaunchedEffect(pagerState, selectedSection) {
        val index = keys.indexOf(selectedSection)
        if (index >= 0 && index != pagerState.currentPage && !pagerState.isScrollInProgress) {
            pagerState.scrollToPage(index)
        }
    }
    return pagerState
}

private const val HEADER_KEY = "section_pane_header"
private const val TAB_ROW_KEY = "section_pane_tab_row"
private const val PAGER_KEY = "section_pane_pager"
