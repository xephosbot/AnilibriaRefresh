package com.xbot.title.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.TraversableNode
import androidx.compose.ui.node.findNearestAncestor
import androidx.compose.ui.node.invalidateMeasurement
import androidx.compose.ui.unit.Constraints

internal fun Modifier.sectionScroll(state: LazyListState): Modifier =
    this then SectionScrollElement(LazyListSectionScrollable(state))

internal fun Modifier.sectionScroll(state: LazyGridState): Modifier =
    this then SectionScrollElement(LazyGridSectionScrollable(state))

internal fun Modifier.sectionScroll(state: ScrollState): Modifier =
    this then SectionScrollElement(ScrollStateSectionScrollable(state))

internal interface SectionScrollable {
    fun dispatchRawDelta(delta: Float): Float

    fun requestScrollToStart()
}

internal interface SectionPageHost {
    fun onScrollableAttached(page: Any, scrollable: SectionScrollable)

    fun onScrollableDetached(page: Any, scrollable: SectionScrollable)

    fun onContentHeightChanged(page: Any, height: Int)
}

internal fun Modifier.sectionPage(host: SectionPageHost, page: Any): Modifier =
    this then SectionPageElement(host, page)

private data class LazyListSectionScrollable(val state: LazyListState) : SectionScrollable {
    override fun dispatchRawDelta(delta: Float): Float = state.dispatchRawDelta(delta)

    override fun requestScrollToStart() = state.requestScrollToItem(0)
}

private data class LazyGridSectionScrollable(val state: LazyGridState) : SectionScrollable {
    override fun dispatchRawDelta(delta: Float): Float = state.dispatchRawDelta(delta)

    override fun requestScrollToStart() = state.requestScrollToItem(0)
}

private data class ScrollStateSectionScrollable(val state: ScrollState) : SectionScrollable {
    override fun dispatchRawDelta(delta: Float): Float = state.dispatchRawDelta(delta)

    override fun requestScrollToStart() {
        state.dispatchRawDelta(-state.value.toFloat())
    }
}

private object SectionPageTraverseKey

private data class SectionPageElement(val host: SectionPageHost, val page: Any) :
    ModifierNodeElement<SectionPageNode>() {
    override fun create() = SectionPageNode(host, page)

    override fun update(node: SectionPageNode) = node.update(host, page)
}

private class SectionPageNode(private var host: SectionPageHost, private var page: Any) :
    Modifier.Node(),
    LayoutModifierNode,
    TraversableNode {

    override val traverseKey: Any = SectionPageTraverseKey

    var scrollable: SectionScrollable? = null
        set(value) {
            if (field == value) return
            field?.let { host.onScrollableDetached(page, it) }
            field = value
            value?.let { host.onScrollableAttached(page, it) }
        }

    fun update(host: SectionPageHost, page: Any) {
        if (this.host == host && this.page == page) return
        scrollable?.let { this.host.onScrollableDetached(this.page, it) }
        this.host = host
        this.page = page
        scrollable?.let { host.onScrollableAttached(page, it) }
        invalidateMeasurement()
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints.copy(minHeight = 0))
        host.onContentHeightChanged(page, placeable.height)
        return layout(constraints.maxWidth, constraints.maxHeight) {
            placeable.place(0, 0)
        }
    }
}

private data class SectionScrollElement(val scrollable: SectionScrollable) :
    ModifierNodeElement<SectionScrollNode>() {
    override fun create() = SectionScrollNode(scrollable)

    override fun update(node: SectionScrollNode) {
        node.scrollable = scrollable
    }
}

private class SectionScrollNode(scrollable: SectionScrollable) : Modifier.Node() {
    private var page: SectionPageNode? = null

    var scrollable: SectionScrollable = scrollable
        set(value) {
            if (field == value) return
            field = value
            page?.scrollable = value
        }

    override fun onAttach() {
        page = findNearestAncestor(SectionPageTraverseKey) as? SectionPageNode
        page?.scrollable = scrollable
    }

    override fun onDetach() {
        page?.takeIf { it.scrollable == scrollable }?.scrollable = null
        page = null
    }
}
