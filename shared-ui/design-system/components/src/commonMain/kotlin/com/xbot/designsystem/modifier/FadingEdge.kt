package com.xbot.designsystem.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import kotlin.math.abs

fun Modifier.fadingEdge(
    startFraction: Float = 0f,
    endFraction: Float = 1f,
    opacity: Float = 1f,
    bottomEdge: Boolean = true
): Modifier = this then FadingEdgeElement(
    startFraction = startFraction.coerceIn(0f, 1f),
    endFraction = endFraction.coerceIn(0f, 1f),
    opacity = opacity,
    bottomEdge = bottomEdge
)

internal enum class FadeSide { Left, Top, Right, Bottom }

internal inline fun DrawScope.withFadingLayer(block: DrawScope.() -> Unit) {
    drawIntoCanvas { canvas ->
        canvas.saveLayer(size.toRect(), Paint())
        block()
        canvas.restore()
    }
}

internal fun DrawScope.drawFade(
    side: FadeSide,
    visibleAt: Float,
    hiddenAt: Float,
    hiddenAlpha: Float = 0f,
    edge: Float = when (side) {
        FadeSide.Left, FadeSide.Top -> 0f
        FadeSide.Right -> size.width
        FadeSide.Bottom -> size.height
    }
) {
    if (abs(visibleAt - hiddenAt) < 1f) return
    val colors = listOf(Color.Black, Color.Black.copy(alpha = hiddenAlpha))
    when (side) {
        FadeSide.Left -> drawRect(
            brush = Brush.horizontalGradient(colors, startX = visibleAt, endX = hiddenAt),
            topLeft = Offset(edge, 0f),
            size = Size(visibleAt - edge, size.height),
            blendMode = BlendMode.DstIn
        )

        FadeSide.Right -> drawRect(
            brush = Brush.horizontalGradient(colors, startX = visibleAt, endX = hiddenAt),
            topLeft = Offset(visibleAt, 0f),
            size = Size(edge - visibleAt, size.height),
            blendMode = BlendMode.DstIn
        )

        FadeSide.Top -> drawRect(
            brush = Brush.verticalGradient(colors, startY = visibleAt, endY = hiddenAt),
            topLeft = Offset(0f, edge),
            size = Size(size.width, visibleAt - edge),
            blendMode = BlendMode.DstIn
        )

        FadeSide.Bottom -> drawRect(
            brush = Brush.verticalGradient(colors, startY = visibleAt, endY = hiddenAt),
            topLeft = Offset(0f, visibleAt),
            size = Size(size.width, edge - visibleAt),
            blendMode = BlendMode.DstIn
        )
    }
}

private data class FadingEdgeElement(
    val startFraction: Float,
    val endFraction: Float,
    val opacity: Float,
    val bottomEdge: Boolean
) : ModifierNodeElement<FadingEdgeNode>() {
    override fun create() = FadingEdgeNode(startFraction, endFraction, opacity, bottomEdge)

    override fun update(node: FadingEdgeNode) {
        node.startFraction = startFraction
        node.endFraction = endFraction
        node.opacity = opacity
        node.bottomEdge = bottomEdge
        node.invalidateDraw()
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "fadingEdge"
        properties["startFraction"] = startFraction
        properties["endFraction"] = endFraction
        properties["opacity"] = opacity
        properties["bottomEdge"] = bottomEdge
    }
}

private class FadingEdgeNode(
    var startFraction: Float,
    var endFraction: Float,
    var opacity: Float,
    var bottomEdge: Boolean
) : Modifier.Node(),
    DrawModifierNode {

    override fun ContentDrawScope.draw() {
        val height = size.height
        val visibleAt = startFraction * height
        val hiddenAt = endFraction * height
        withFadingLayer {
            this@draw.drawContent()
            if (bottomEdge) {
                drawFade(FadeSide.Bottom, visibleAt, hiddenAt, hiddenAlpha = 1f - opacity)
            } else {
                drawFade(
                    FadeSide.Top,
                    height - visibleAt,
                    height - hiddenAt,
                    hiddenAlpha =
                        1f - opacity
                )
            }
        }
    }
}
