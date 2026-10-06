package com.xbot.designsystem.modifier

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MarqueeDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.requireDensity
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun Modifier.marquee(
    iterations: Int = Int.MAX_VALUE,
    initialDelayMillis: Int = MarqueeDefaults.RepeatDelayMillis,
    repeatDelayMillis: Int = MarqueeDefaults.RepeatDelayMillis,
    spacing: Dp = MarqueeModifierDefaults.Spacing,
    velocity: Dp = MarqueeDefaults.Velocity,
    fadingEdgeWidth: Dp = MarqueeModifierDefaults.FadingEdgeWidth
): Modifier = this then MarqueeElement(
    iterations = iterations,
    initialDelayMillis = initialDelayMillis,
    repeatDelayMillis = repeatDelayMillis,
    spacing = spacing,
    velocity = velocity,
    fadingEdgeWidth = fadingEdgeWidth
)

object MarqueeModifierDefaults {
    val Spacing = 32.dp
    val FadingEdgeWidth = 16.dp
}

private data class MarqueeElement(
    val iterations: Int,
    val initialDelayMillis: Int,
    val repeatDelayMillis: Int,
    val spacing: Dp,
    val velocity: Dp,
    val fadingEdgeWidth: Dp
) : ModifierNodeElement<MarqueeNode>() {
    override fun create() = MarqueeNode(
        iterations,
        initialDelayMillis,
        repeatDelayMillis,
        spacing,
        velocity,
        fadingEdgeWidth
    )

    override fun update(node: MarqueeNode) {
        node.update(
            iterations,
            initialDelayMillis,
            repeatDelayMillis,
            spacing,
            velocity,
            fadingEdgeWidth
        )
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "marquee"
        properties["iterations"] = iterations
        properties["initialDelayMillis"] = initialDelayMillis
        properties["repeatDelayMillis"] = repeatDelayMillis
        properties["spacing"] = spacing
        properties["velocity"] = velocity
        properties["fadingEdgeWidth"] = fadingEdgeWidth
    }
}

private class MarqueeNode(
    private var iterations: Int,
    private var initialDelayMillis: Int,
    private var repeatDelayMillis: Int,
    private var spacing: Dp,
    private var velocity: Dp,
    private var fadingEdgeWidth: Dp
) : Modifier.Node(),
    LayoutModifierNode,
    DrawModifierNode {

    private val offset = Animatable(0f)
    private var contentWidth by mutableIntStateOf(0)
    private var containerWidth by mutableIntStateOf(0)
    private var animationJob: Job? = null

    private val spacingPx: Float
        get() = with(requireDensity()) { spacing.toPx() }

    override fun onAttach() {
        restartAnimation()
    }

    fun update(
        iterations: Int,
        initialDelayMillis: Int,
        repeatDelayMillis: Int,
        spacing: Dp,
        velocity: Dp,
        fadingEdgeWidth: Dp
    ) {
        val animationChanged = this.iterations != iterations ||
            this.initialDelayMillis != initialDelayMillis ||
            this.repeatDelayMillis != repeatDelayMillis ||
            this.spacing != spacing ||
            this.velocity != velocity
        this.iterations = iterations
        this.initialDelayMillis = initialDelayMillis
        this.repeatDelayMillis = repeatDelayMillis
        this.spacing = spacing
        this.velocity = velocity
        this.fadingEdgeWidth = fadingEdgeWidth
        if (animationChanged) restartAnimation()
        invalidateDraw()
    }

    private fun restartAnimation() {
        if (!isAttached) return
        animationJob?.cancel()
        animationJob = coroutineScope.launch {
            snapshotFlow {
                if (contentWidth > containerWidth) contentWidth + spacingPx else 0f
            }.collectLatest { distance ->
                offset.snapTo(0f)
                if (distance > 0f) runIterations(distance)
            }
        }
    }

    private suspend fun runIterations(distance: Float) {
        val velocityPx = with(requireDensity()) { velocity.toPx() }.coerceAtLeast(1f)
        val durationMillis = (distance / velocityPx * MILLIS_IN_SECOND).toInt()
        delay(initialDelayMillis.toLong())
        var iteration = 0
        while (iteration < iterations) {
            offset.animateTo(distance, tween(durationMillis, easing = LinearEasing))
            offset.snapTo(0f)
            iteration++
            if (iteration < iterations) delay(repeatDelayMillis.toLong())
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(
            constraints.copy(minWidth = 0, maxWidth = Constraints.Infinity)
        )
        val width = constraints.constrainWidth(placeable.width)
        contentWidth = placeable.width
        containerWidth = width
        return layout(width, placeable.height) { placeable.placeRelative(0, 0) }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int = 0

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int = measurable.maxIntrinsicWidth(height)

    override fun ContentDrawScope.draw() {
        val content = contentWidth.toFloat()
        val container = containerWidth.toFloat()
        if (content <= container) {
            drawContent()
            return
        }
        val scrolled = offset.value
        val gap = spacingPx
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val direction = if (isRtl) 1f else -1f
        val containerLeft = if (isRtl) content - container else 0f
        val containerRight = containerLeft + container
        val hiddenAtStart = if (content - scrolled > 0f) scrolled else 0f
        val copyEndHidden = if (content + gap - scrolled < container) {
            2 * content + gap - scrolled - container
        } else {
            0f
        }
        val hiddenAtEnd = maxOf(content - scrolled - container, copyEndHidden, 0f)
        val edge = fadingEdgeWidth.toPx().coerceAtMost(container / 2)
        val fadeStart = hiddenAtStart.coerceAtMost(edge)
        val fadeEnd = hiddenAtEnd.coerceAtMost(edge)
        val fadeLeft = if (isRtl) fadeEnd else fadeStart
        val fadeRight = if (isRtl) fadeStart else fadeEnd

        clipRect(left = containerLeft, right = containerRight) {
            withFadingLayer {
                translate(left = direction * scrolled) { this@draw.drawContent() }
                translate(left = direction * (scrolled - content - gap)) { this@draw.drawContent() }
                drawFade(
                    side = FadeSide.Left,
                    visibleAt = containerLeft + fadeLeft,
                    hiddenAt = containerLeft,
                    edge = containerLeft
                )
                drawFade(
                    side = FadeSide.Right,
                    visibleAt = containerRight - fadeRight,
                    hiddenAt = containerRight,
                    edge = containerRight
                )
            }
        }
    }
}

private const val MILLIS_IN_SECOND = 1_000
