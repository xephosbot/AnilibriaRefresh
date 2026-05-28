package com.xbot.navigation.scaffold

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateMeasurement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize

/**
 * A modifier that caches the measured size of the component and optionally reuses it. For example,
 * this can be used to maintain the size of an element that contains moveable content.
 *
 * @param useCachedSize If true, the modifier will use the previously measured and cached size
 *   (if available) instead of the current size. If false, it measures normally and caches the new
 *   size.
 */
internal fun Modifier.cacheSize(useCachedSize: Boolean): Modifier =
    this.then(CacheSizeElement(useCachedSize))

private data class CacheSizeElement(
    val useCachedSize: Boolean,
) : ModifierNodeElement<CacheSizeNode>() {

    override fun create() = CacheSizeNode(useCachedSize)

    override fun update(node: CacheSizeNode) {
        node.useCachedSize = useCachedSize
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "cacheSize"
        properties["useCachedSize"] = useCachedSize
    }
}

private class CacheSizeNode(
    useCachedSize: Boolean,
) : Modifier.Node(), LayoutModifierNode {

    var useCachedSize: Boolean = useCachedSize
        set(value) {
            if (field != value) {
                field = value
                invalidateMeasurement()
            }
        }

    private var isSizeCached = false
    private var cachedSize: IntSize = IntSize.Zero

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        val currentSize = IntSize(placeable.width, placeable.height)

        val size = if (useCachedSize && isSizeCached) cachedSize else currentSize

        cachedSize = size
        isSizeCached = true

        return layout(size.width, size.height) {
            placeable.placeRelative(0, 0)
        }
    }
}
