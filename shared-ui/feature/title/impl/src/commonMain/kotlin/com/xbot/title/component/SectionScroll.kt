package com.xbot.title.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.ModifierNodeElement

internal interface SectionScrollable {
    fun dispatchRawDelta(delta: Float): Float

    fun requestScrollToStart()
}

context(scope: PaneSectionScope)
internal fun Modifier.sectionScroll(state: LazyListState): Modifier =
    this then scope.connectScroll(LazyListSectionScrollable(state))

context(scope: PaneSectionScope)
internal fun Modifier.sectionScroll(state: LazyGridState): Modifier =
    this then scope.connectScroll(LazyGridSectionScrollable(state))

context(scope: PaneSectionScope)
internal fun Modifier.sectionScroll(state: ScrollState): Modifier =
    this then scope.connectScroll(ScrollStateSectionScrollable(state))

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

@Stable
internal class SectionScrollRegistry<K : Any>(
    private val shouldResetOnAttach: (K) -> Boolean = { false }
) {
    private val scrollables = mutableMapOf<K, SectionScrollable>()

    val contentHeights = mutableStateMapOf<K, Int>()

    operator fun get(key: K): SectionScrollable? = scrollables[key]

    fun attach(key: K, scrollable: SectionScrollable) {
        scrollables[key] = scrollable
        if (shouldResetOnAttach(key)) scrollable.requestScrollToStart()
    }

    fun detach(key: K, scrollable: SectionScrollable) {
        if (scrollables[key] == scrollable) scrollables.remove(key)
    }

    fun reportContentHeight(key: K, height: Int) {
        if (contentHeights[key] != height) contentHeights[key] = height
    }
}

internal fun <K : Any> SectionScrollRegistry<K>.connect(
    key: K,
    scrollable: SectionScrollable
): Modifier = SectionScrollElement(this, key, scrollable)

private data class SectionScrollElement<K : Any>(
    val registry: SectionScrollRegistry<K>,
    val key: K,
    val scrollable: SectionScrollable
) : ModifierNodeElement<SectionScrollNode<K>>() {
    override fun create() = SectionScrollNode(registry, key, scrollable)

    override fun update(node: SectionScrollNode<K>) = node.update(registry, key, scrollable)
}

private class SectionScrollNode<K : Any>(
    private var registry: SectionScrollRegistry<K>,
    private var key: K,
    private var scrollable: SectionScrollable
) : Modifier.Node() {
    override fun onAttach() = registry.attach(key, scrollable)

    override fun onDetach() = registry.detach(key, scrollable)

    fun update(registry: SectionScrollRegistry<K>, key: K, scrollable: SectionScrollable) {
        if (this.registry == registry && this.key == key && this.scrollable == scrollable) return
        if (isAttached) this.registry.detach(this.key, this.scrollable)
        this.registry = registry
        this.key = key
        this.scrollable = scrollable
        if (isAttached) registry.attach(key, scrollable)
    }
}
