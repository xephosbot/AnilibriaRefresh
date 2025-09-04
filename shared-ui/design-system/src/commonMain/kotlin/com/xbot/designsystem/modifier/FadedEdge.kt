package com.xbot.designsystem.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.fadedEdge(
    edgeHeight: Dp = DefaultFadingEdgeHeight,
    opacity: Float = 1.0f,
    bottomEdge: Boolean = true,
): Modifier = fadedEdgeInternal(
    opacity = opacity,
    bottomEdge = bottomEdge
) { size -> edgeHeight.toPx() }

fun Modifier.fadedEdge(
    edgeHeightRatio: Float = 0.5f,
    opacity: Float = 1.0f,
    bottomEdge: Boolean = true
): Modifier = fadedEdgeInternal(
    opacity = opacity,
    bottomEdge = bottomEdge
) { size -> size.height * edgeHeightRatio }


private fun Modifier.fadedEdgeInternal(
    opacity: Float,
    bottomEdge: Boolean,
    edgeHeightPx: CacheDrawScope.(Size) -> Float
): Modifier = this.then(
    graphicsLayer {
        compositingStrategy = CompositingStrategy.Offscreen
    }.drawWithCache {
        val edgeHeight = edgeHeightPx(size).coerceAtLeast(0f)

        val brush = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent.copy(alpha = 1f - opacity),
                Color.Black
            ),
            startY = if (bottomEdge) size.height else 0f,
            endY = if (bottomEdge) size.height - edgeHeight else edgeHeight
        )

        onDrawWithContent {
            drawContent()
            drawRect(
                topLeft = Offset(
                    x = 0f,
                    y = if (bottomEdge) size.height - edgeHeight else 0f
                ),
                size = Size(width = size.width, height = edgeHeight),
                brush = brush,
                blendMode = BlendMode.DstIn
            )
        }
    }
)


private val DefaultFadingEdgeHeight = 96.dp
