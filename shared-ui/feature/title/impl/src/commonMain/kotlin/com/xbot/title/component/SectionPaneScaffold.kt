package com.xbot.title.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculateThreePaneScaffoldValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.WindowInsetsRulers
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.theme.LocalMargins
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@Immutable
internal data class PaneSection<K : Any>(
    val key: K,
    val role: ThreePaneScaffoldRole,
    val title: String
)

@Stable
internal interface PaneSectionScope {
    val listState: LazyListState
    val contentPadding: PaddingValues
    val isActive: Boolean
}

@Stable
internal interface PaneHeaderScope {
    val scrollState: ScrollState
    val contentPadding: PaddingValues
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
        val isSplitByHinge =
            directive.excludedBounds.isNotEmpty() && directive.maxHorizontalPartitions < 2
        directive.copy(
            maxHorizontalPartitions = if (isSplitByHinge) 2 else directive.maxHorizontalPartitions,
            horizontalPartitionSpacerSize = if (isSplitByHinge) {
                24.dp
            } else {
                directive.horizontalPartitionSpacerSize
            },
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
    header: @Composable PaneHeaderScope.() -> Unit = {},
    content: @Composable PaneSectionScope.(section: K) -> Unit
) {
    val scaffoldDirective = directive.fitTo(sections)
    val scaffoldValue = calculateThreePaneScaffoldValue(
        maxHorizontalPartitions = scaffoldDirective.maxHorizontalPartitions,
        adaptStrategies = SupportingPaneScaffoldDefaults.adaptStrategies(),
        destinationHistory = emptyList(),
        maxVerticalPartitions = scaffoldDirective.maxVerticalPartitions
    )
    val layout = scaffoldValue.resolve(sections)
    val isMultiPane = scaffoldValue.hasSideBySidePanes()
    val mainScrollState = rememberScrollState()
    val context = SectionPaneContext(
        listStates = sections.associate { section ->
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
                        sections = layout.main,
                        scrollState = mainScrollState,
                        context = context,
                        header = header,
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

@Composable
internal fun rememberPaneHeaderScope(
    scrollState: ScrollState = rememberScrollState(),
    contentPadding: PaddingValues = PaddingValues()
): PaneHeaderScope = remember(scrollState, contentPadding) {
    PaneHeaderScopeImpl(scrollState, contentPadding)
}

private class PaneHeaderScopeImpl(
    override val scrollState: ScrollState,
    override val contentPadding: PaddingValues
) : PaneHeaderScope

private class SectionLayout<K : Any>(
    val main: List<PaneSection<K>>,
    val supporting: List<PaneSection<K>>,
    val extra: List<PaneSection<K>>
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun PaneScaffoldDirective.fitTo(tabSections: List<PaneSection<*>>): PaneScaffoldDirective {
    val sidePaneCount = tabSections.map { it.role }
        .distinct()
        .count { it != SupportingPaneScaffoldRole.Main }
    return copy(
        maxHorizontalPartitions = minOf(maxHorizontalPartitions, 1 + sidePaneCount),
        maxVerticalPartitions = if (tabSections.isEmpty()) 1 else maxVerticalPartitions
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <K : Any> ThreePaneScaffoldValue.resolve(
    tabSections: List<PaneSection<K>>
): SectionLayout<K> {
    fun withRoles(vararg roles: ThreePaneScaffoldRole) = tabSections.filter { it.role in roles }

    val supportingValue = this[SupportingPaneScaffoldRole.Supporting]
    val isSupportingSideBySide = supportingValue == PaneAdaptedValue.Expanded
    val isExtraSideBySide = this[SupportingPaneScaffoldRole.Extra] == PaneAdaptedValue.Expanded
    return when {
        isSupportingSideBySide && isExtraSideBySide -> SectionLayout(
            main = withRoles(SupportingPaneScaffoldRole.Main),
            supporting = withRoles(SupportingPaneScaffoldRole.Supporting),
            extra = withRoles(SupportingPaneScaffoldRole.Extra)
        )

        isSupportingSideBySide -> SectionLayout(
            main = withRoles(SupportingPaneScaffoldRole.Main),
            supporting = withRoles(
                SupportingPaneScaffoldRole.Supporting,
                SupportingPaneScaffoldRole.Extra
            ),
            extra = emptyList()
        )

        supportingValue != PaneAdaptedValue.Hidden -> SectionLayout(
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
    sections: List<PaneSection<K>>,
    scrollState: ScrollState,
    context: SectionPaneContext<K>,
    header: @Composable PaneHeaderScope.() -> Unit,
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
        val headerFirstConnection = remember(scrollState) {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
                    if (available.y < 0f) {
                        Offset(0f, -scrollState.dispatchRawDelta(-available.y))
                    } else {
                        Offset.Zero
                    }
            }
        }
        val headerScope = rememberPaneHeaderScope(scrollState, innerPadding)
        val overscrollEffect = rememberOverscrollEffect()
        val combinedScrollState = rememberScrollableState { delta ->
            val listState = pagerState
                ?.let { sections.getOrNull(it.currentPage) }
                ?.let { context.listStates[it.key] }
            if (delta > 0f) {
                val consumedByHeader = scrollState.dispatchRawDelta(delta)
                consumedByHeader + (listState?.dispatchRawDelta(delta - consumedByHeader) ?: 0f)
            } else {
                val consumedByList = listState?.dispatchRawDelta(delta) ?: 0f
                consumedByList + scrollState.dispatchRawDelta(delta - consumedByList)
            }
        }

        BoxWithConstraints {
            val tabsHeight = (maxHeight - innerPadding.calculateTopPadding()).coerceAtLeast(0.dp)

            Column(
                modifier = Modifier
                    .overscroll(overscrollEffect)
                    .scrollable(
                        state = combinedScrollState,
                        orientation = Orientation.Vertical,
                        overscrollEffect = overscrollEffect,
                        reverseDirection = true
                    )
                    .verticalScroll(scrollState, overscrollEffect = null, enabled = false)
            ) {
                headerScope.header()

                if (pagerState != null) {
                    SectionTabs(
                        modifier = Modifier
                            .padding(top = if (sections.size > 1) 16.dp else 0.dp)
                            .height(tabsHeight)
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

    SectionTabs(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            ),
        sections = sections,
        pagerState = pagerState,
        pageContentPadding = PaddingValues(),
        context = context
    )
}

@Composable
private fun <K : Any> SectionTabs(
    sections: List<PaneSection<K>>,
    pagerState: PagerState,
    pageContentPadding: PaddingValues,
    context: SectionPaneContext<K>,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    Column(modifier = modifier) {
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
            pageContentPadding = pageContentPadding,
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
    content(rememberPaneSectionScope(listState, contentPadding, isActive), section)
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
