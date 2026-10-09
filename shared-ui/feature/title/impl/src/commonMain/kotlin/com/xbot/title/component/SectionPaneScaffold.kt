package com.xbot.title.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberOverscrollEffect
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.WindowInsetsRulers
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.xbot.designsystem.theme.LocalMargins
import kotlin.math.floor
import kotlin.math.roundToInt
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
    val contentPadding: PaddingValues
    val isActive: Boolean

    fun connectScroll(scrollable: SectionScrollable): Modifier
}

@Stable
internal interface PaneHeaderScope {
    val scrollOffset: Int
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
    val collapsingState = rememberCollapsingState()
    val context = SectionPaneContext(
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
                        collapsingState = collapsingState,
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
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val selectedIndex = pagerState.currentPage
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
        sections.forEachIndexed { index, section ->
            Tab(
                selected = index == selectedIndex,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
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
    val selectedSection: K,
    val onSectionSelect: (K) -> Unit,
    val content: @Composable PaneSectionScope.(section: K) -> Unit
)

@Composable
internal fun rememberPaneSectionScope(
    contentPadding: PaddingValues = PaddingValues(),
    isActive: Boolean = true
): PaneSectionScope = remember(contentPadding, isActive) {
    object : PaneSectionScope {
        override val contentPadding: PaddingValues = contentPadding
        override val isActive: Boolean = isActive
        override fun connectScroll(scrollable: SectionScrollable): Modifier = Modifier
    }
}

private class PaneSectionScopeImpl<K : Any>(
    private val registry: SectionScrollRegistry<K>,
    private val key: K,
    override val contentPadding: PaddingValues,
    override val isActive: Boolean
) : PaneSectionScope {
    override fun connectScroll(scrollable: SectionScrollable): Modifier =
        registry.connect(key, scrollable)
}

@Composable
internal fun rememberPaneHeaderScope(
    contentPadding: PaddingValues = PaddingValues()
): PaneHeaderScope = remember(contentPadding) {
    PaneHeaderScopeImpl(scrollOffset = 0, contentPadding = contentPadding)
}

private class PaneHeaderScopeImpl(
    override val scrollOffset: Int,
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
    collapsingState: CollapsingState,
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
        val headerFirstConnection = remember(collapsingState) {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
                    if (available.y < 0f) {
                        Offset(0f, -collapsingState.dispatch(-available.y))
                    } else {
                        Offset.Zero
                    }
            }
        }
        val headerScope = remember(collapsingState, innerPadding) {
            object : PaneHeaderScope {
                override val scrollOffset: Int get() = collapsingState.visibleOffset.roundToInt()
                override val contentPadding: PaddingValues = innerPadding
            }
        }
        val currentSections by rememberUpdatedState(sections)
        val registry = remember(collapsingState, pagerState) {
            SectionScrollRegistry<K> { key ->
                val activeKey = pagerState?.let { currentSections.getOrNull(it.currentPage) }?.key
                key != activeKey && !collapsingState.isCollapsed
            }
        }
        SideEffect {
            collapsingState.limit = {
                if (pagerState == null) {
                    collapsingState.headerHeight - collapsingState.viewportHeight
                } else {
                    val position = pagerState.currentPage + pagerState.currentPageOffsetFraction
                    val page = floor(position).toInt()
                    lerp(
                        collapsingState.limitFor(sections.getOrNull(page)?.key, registry),
                        collapsingState.limitFor(sections.getOrNull(page + 1)?.key, registry),
                        position - page
                    )
                }
            }
        }
        val overscrollEffect = rememberOverscrollEffect()
        val combinedScrollState = rememberScrollableState { delta ->
            val scrollable = pagerState
                ?.let { sections.getOrNull(it.currentPage) }
                ?.let { registry[it.key] }
            if (delta > 0f) {
                val consumedByHeader = collapsingState.dispatch(delta)
                consumedByHeader + (scrollable?.dispatchRawDelta(delta - consumedByHeader) ?: 0f)
            } else {
                val consumedByContent = scrollable?.dispatchRawDelta(delta) ?: 0f
                consumedByContent + collapsingState.dispatch(delta - consumedByContent)
            }
        }

        Layout(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .overscroll(overscrollEffect)
                .scrollable(
                    state = combinedScrollState,
                    orientation = Orientation.Vertical,
                    overscrollEffect = overscrollEffect,
                    reverseDirection = true
                ),
            content = {
                Column {
                    headerScope.header()
                    if (pagerState != null && sections.size > 1) {
                        PaneSectionTabRow(
                            modifier = Modifier.padding(top = 16.dp),
                            sections = sections,
                            pagerState = pagerState
                        )
                    }
                }
                if (pagerState != null) {
                    SectionPager(
                        modifier = Modifier.nestedScroll(headerFirstConnection),
                        sections = sections,
                        pagerState = pagerState,
                        pageContentPadding = PaddingValues(
                            bottom = innerPadding.calculateBottomPadding()
                        ),
                        context = context,
                        registry = registry
                    )
                }
            }
        ) { measurables, constraints ->
            val looseConstraints = constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)
            val headerPlaceable = measurables[0].measure(looseConstraints)
            val pagerPlaceable = measurables.getOrNull(1)?.measure(
                Constraints.fixed(constraints.maxWidth, constraints.maxHeight)
            )
            collapsingState.headerHeight = headerPlaceable.height.toFloat()
            collapsingState.viewportHeight = constraints.maxHeight.toFloat()

            layout(constraints.maxWidth, constraints.maxHeight) {
                val y = -collapsingState.visibleOffset.roundToInt()
                headerPlaceable.place(0, y)
                pagerPlaceable?.place(0, y + headerPlaceable.height)
            }
        }
    }
}

@Stable
private class CollapsingState(initialOffset: Float = 0f) {
    var offset by mutableFloatStateOf(initialOffset)
        private set

    var headerHeight = Float.POSITIVE_INFINITY

    var viewportHeight = 0f

    var limit: () -> Float = { headerHeight }

    val isCollapsed: Boolean
        get() = offset >= headerHeight

    val visibleOffset: Float
        get() = offset.coerceIn(0f, limit().coerceAtLeast(0f))

    fun dispatch(delta: Float): Float {
        if (delta == 0f) return 0f
        val currentOffset = visibleOffset
        val newOffset = (currentOffset + delta).coerceIn(0f, limit().coerceAtLeast(0f))
        offset = newOffset
        return newOffset - currentOffset
    }

    fun <K : Any> limitFor(key: K?, registry: SectionScrollRegistry<K>): Float {
        val contentHeight = key?.let { registry.contentHeights[it] }?.toFloat() ?: viewportHeight
        return headerHeight - (viewportHeight - contentHeight).coerceAtLeast(0f)
    }

    companion object {
        val Saver: Saver<CollapsingState, Float> = Saver(
            save = { it.offset },
            restore = { CollapsingState(it) }
        )
    }
}

@Composable
private fun rememberCollapsingState(): CollapsingState =
    rememberSaveable(saver = CollapsingState.Saver) { CollapsingState() }

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
    val registry = remember { SectionScrollRegistry<K>() }

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
        context = context,
        registry = registry
    )
}

@Composable
private fun <K : Any> SectionTabs(
    sections: List<PaneSection<K>>,
    pagerState: PagerState,
    pageContentPadding: PaddingValues,
    context: SectionPaneContext<K>,
    registry: SectionScrollRegistry<K>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (sections.size > 1) {
            PaneSectionTabRow(sections = sections, pagerState = pagerState)
        }
        SectionPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            sections = sections,
            pagerState = pagerState,
            pageContentPadding = pageContentPadding,
            context = context,
            registry = registry
        )
    }
}

@Composable
private fun <K : Any> SectionPager(
    sections: List<PaneSection<K>>,
    pagerState: PagerState,
    pageContentPadding: PaddingValues,
    context: SectionPaneContext<K>,
    registry: SectionScrollRegistry<K>,
    modifier: Modifier = Modifier
) {
    key(pagerState) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
        ) { index ->
            val section = sections[index].key
            val isActive = pagerState.settledPage == index
            val scope = remember(registry, section, pageContentPadding, isActive) {
                PaneSectionScopeImpl(registry, section, pageContentPadding, isActive)
            }
            Layout(content = { context.content(scope, section) }) { measurables, constraints ->
                val placeables = measurables.map { it.measure(constraints.copy(minHeight = 0)) }
                registry.reportContentHeight(section, placeables.maxOfOrNull { it.height } ?: 0)
                layout(constraints.maxWidth, constraints.maxHeight) {
                    placeables.forEach { it.place(0, 0) }
                }
            }
        }
    }
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
