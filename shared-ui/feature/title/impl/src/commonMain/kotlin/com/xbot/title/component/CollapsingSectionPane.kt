package com.xbot.title.component

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.xbot.designsystem.utils.only
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
internal fun <K : Any> CollapsingSectionPane(
    sections: List<PaneSection<K>>,
    selectedSection: K,
    onSectionSelect: (K) -> Unit,
    headerState: CollapsingHeaderState,
    header: @Composable PaneHeaderScope.() -> Unit,
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable PaneSectionScope.(section: K) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        val pagerState = if (sections.isNotEmpty()) {
            rememberSectionPagerState(sections, selectedSection, onSectionSelect)
        } else {
            null
        }
        val pages = sections.map { it.key }
        val pagesState = remember(headerState, pagerState, pages) {
            CollapsingPagesState(headerState, pagerState, pages)
        }
        val headerScope = remember(pagesState, innerPadding) {
            object : PaneHeaderScope {
                override val scrollOffset: Int get() = pagesState.visibleOffset.roundToInt()
                override val contentPadding: PaddingValues = innerPadding
            }
        }
        val overscrollEffect = rememberOverscrollEffect()

        Layout(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .overscroll(overscrollEffect)
                .scrollable(
                    state = pagesState.scrollableState,
                    orientation = Orientation.Vertical,
                    overscrollEffect = overscrollEffect,
                    reverseDirection = true
                ),
            content = {
                Column {
                    headerScope.header()
                    if (pagerState != null && sections.size > 1) {
                        SectionTabRow(
                            modifier = Modifier.padding(top = 16.dp),
                            sections = sections,
                            pagerState = pagerState,
                            contentPadding = innerPadding only WindowInsetsSides.Horizontal
                        )
                    }
                }
                if (pagerState != null) {
                    SectionPager(
                        modifier = Modifier.nestedScroll(pagesState.nestedScrollConnection),
                        sections = sections,
                        pagerState = pagerState,
                        pageModifier = { page -> Modifier.sectionPage(pagesState, page) },
                        pageContentPadding = innerPadding only
                            (WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
                        content = content
                    )
                }
            }
        ) { measurables, constraints ->
            val headerPlaceable = measurables[0].measure(
                constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)
            )
            val pagerPlaceable = measurables.getOrNull(1)?.measure(
                Constraints.fixed(constraints.maxWidth, constraints.maxHeight)
            )
            pagesState.headerHeight = headerPlaceable.height
            pagesState.viewportHeight = constraints.maxHeight

            layout(constraints.maxWidth, constraints.maxHeight) {
                val y = -pagesState.visibleOffset.roundToInt()
                headerPlaceable.place(0, y)
                pagerPlaceable?.place(0, y + headerPlaceable.height)
            }
        }
    }
}

@Stable
internal class CollapsingHeaderState(initialOffset: Float = 0f) {
    var offset by mutableFloatStateOf(initialOffset)
        internal set

    companion object {
        val Saver: Saver<CollapsingHeaderState, Float> = Saver(
            save = { it.offset },
            restore = { CollapsingHeaderState(it) }
        )
    }
}

@Composable
internal fun rememberCollapsingHeaderState(): CollapsingHeaderState =
    rememberSaveable(saver = CollapsingHeaderState.Saver) { CollapsingHeaderState() }

@Stable
private class CollapsingPagesState(
    private val headerState: CollapsingHeaderState,
    private val pagerState: PagerState?,
    private val pages: List<Any>
) : SectionPageHost {
    var headerHeight = 0
    var viewportHeight = 0

    private val scrollables = mutableMapOf<Any, SectionScrollable>()
    private val contentHeights = mutableStateMapOf<Any, Int>()

    private val currentPage: Any?
        get() = pagerState?.let { pages.getOrNull(it.currentPage) }

    private val isHeaderCollapsed: Boolean
        get() = headerState.offset >= headerHeight

    private val offsetLimit: Float
        get() {
            val pagerState =
                pagerState ?: return (headerHeight - viewportHeight).coerceAtLeast(0).toFloat()
            val position = pagerState.currentPage + pagerState.currentPageOffsetFraction
            val index = floor(position).toInt()
            return lerp(
                offsetLimitFor(pages.getOrNull(index)),
                offsetLimitFor(pages.getOrNull(index + 1)),
                position - index
            ).coerceAtLeast(0f)
        }

    val visibleOffset: Float
        get() = headerState.offset.coerceIn(0f, offsetLimit)

    val scrollableState = ScrollableState { delta ->
        val scrollable = currentPage?.let(scrollables::get)
        if (delta > 0f) {
            val consumedByHeader = scrollHeader(delta)
            consumedByHeader + (scrollable?.dispatchRawDelta(delta - consumedByHeader) ?: 0f)
        } else {
            val consumedByContent = scrollable?.dispatchRawDelta(delta) ?: 0f
            consumedByContent + scrollHeader(delta - consumedByContent)
        }
    }

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
            if (available.y < 0f) Offset(0f, -scrollHeader(-available.y)) else Offset.Zero
    }

    override fun onScrollableAttached(page: Any, scrollable: SectionScrollable) {
        scrollables[page] = scrollable
        if (page != currentPage && !isHeaderCollapsed) scrollable.requestScrollToStart()
    }

    override fun onScrollableDetached(page: Any, scrollable: SectionScrollable) {
        if (scrollables[page] == scrollable) scrollables.remove(page)
    }

    override fun onContentHeightChanged(page: Any, height: Int) {
        if (contentHeights[page] != height) contentHeights[page] = height
    }

    private fun scrollHeader(delta: Float): Float {
        if (delta == 0f) return 0f
        val current = visibleOffset
        val target = (current + delta).coerceIn(0f, offsetLimit)
        headerState.offset = target
        return target - current
    }

    private fun offsetLimitFor(page: Any?): Float {
        val contentHeight = page?.let(contentHeights::get) ?: viewportHeight
        return (headerHeight - (viewportHeight - contentHeight).coerceAtLeast(0)).toFloat()
    }
}
