package com.xbot.designsystem.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.fadedEdge(
    startHeight: Dp,
    endHeight: Dp,
    opacity: Float = 1.0f,
    bottomEdge: Boolean = true,
): Modifier = fadedEdgeInternal(
    opacity = opacity,
    bottomEdge = bottomEdge
) { size ->
    val startPx = startHeight.toPx()
    val endPx = endHeight.toPx()

    val start = (startPx / size.height).coerceIn(0f, 1f)
    val end = (endPx / size.height).coerceIn(0f, 1f)

    start to end
}

fun Modifier.fadedEdge(
    startFraction: Float = 0f,
    endFraction: Float = 1f,
    opacity: Float = 1.0f,
    bottomEdge: Boolean = true
): Modifier = fadedEdgeInternal(
    opacity = opacity,
    bottomEdge = bottomEdge
) { size ->
    startFraction.coerceIn(0f, 1f) to endFraction.coerceIn(0f, 1f)
}

private fun Modifier.fadedEdgeInternal(
    opacity: Float,
    bottomEdge: Boolean,
    edgeRangeProvider: Density.(Size) -> Pair<Float, Float>
): Modifier = graphicsLayer {
        compositingStrategy = CompositingStrategy.Offscreen
    }.drawWithCache {
        val (start, end) = edgeRangeProvider(size)
        val brush = Brush.verticalGradient(
            colorStops = arrayOf(
                0f to Color.Black,
                start to Color.Black,
                end to Color.Transparent.copy(alpha = 1f - opacity),
                1f to Color.Transparent.copy(alpha = 1f - opacity)
            )
        )

        onDrawWithContent {
            drawContent()
            scale(scaleX = 1f, scaleY = if (bottomEdge) 1f else -1f) {
                drawRect(
                    topLeft = Offset.Zero,
                    size = size,
                    brush = brush,
                    blendMode = BlendMode.DstIn
                )
            }
        }
    }


private val DefaultFadingEdgeHeight = 96.dp
