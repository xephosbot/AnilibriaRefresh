package com.xbot.designsystem.effects

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
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
) = graphicsLayer {
    compositingStrategy = CompositingStrategy.Offscreen
}.drawWithCache {
    val edgeHeightPx = edgeHeight.toPx()
    drawFadedEdge(edgeHeightPx, opacity, bottomEdge)
}

fun Modifier.fadedEdge(
    edgeHeightRatio: Float = 0.5f,
    opacity: Float = 1.0f,
    bottomEdge: Boolean = true
) = graphicsLayer {
     compositingStrategy = CompositingStrategy.Offscreen
}.drawWithCache {
    val edgeHeightPx = size.width * edgeHeightRatio
    drawFadedEdge(edgeHeightPx, opacity, bottomEdge)
}

private fun CacheDrawScope.drawFadedEdge(edgeHeight: Float, opacity: Float, bottomEdge: Boolean): DrawResult {
    val brush = Brush.verticalGradient(
        colors = listOf(Color.Transparent.copy(alpha = 1f - opacity), Color.Black),
        startY = if (bottomEdge) size.height else 0f,
        endY = if (bottomEdge) size.height - edgeHeight else edgeHeight,
    )
    return onDrawWithContent {
        drawContent()
        drawRect(
            topLeft = Offset(0f, if (bottomEdge) size.height - edgeHeight else 0f),
            size = Size(size.width, edgeHeight),
            brush = brush,
            blendMode = BlendMode.DstIn,
        )
    }
}

private val DefaultFadingEdgeHeight = 96.dp
