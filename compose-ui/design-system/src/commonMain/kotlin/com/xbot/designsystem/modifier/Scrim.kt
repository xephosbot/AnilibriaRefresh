package com.xbot.designsystem.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun Modifier.scrim(
    edgeHeight: Dp,
    opacity: Float = 0.8f,
    bottomEdge: Boolean = true
): Modifier = scrimInternal(opacity, bottomEdge) { size -> edgeHeight.toPx() }

fun Modifier.scrim(
    edgeHeightRatio: Float = 0.5f,
    opacity: Float = 0.8f,
    bottomEdge: Boolean = true
): Modifier = scrimInternal(opacity, bottomEdge) { size -> size.height * edgeHeightRatio }

private fun Modifier.scrimInternal(
    opacity: Float,
    bottomEdge: Boolean,
    edgeHeightPx: CacheDrawScope.(Size) -> Float
): Modifier = this.drawWithCache {
    val edgeHeight = edgeHeightPx(size).coerceAtLeast(0f)

    val brush = Brush.verticalGradient(
        colors = listOf(
            Color.Black.copy(alpha = opacity),
            Color.Transparent
        ),
        startY = if (bottomEdge) size.height else 0f,
        endY = if (bottomEdge) size.height - edgeHeight else edgeHeight
    )

    val topLeft = Offset(
        x = 0f,
        y = if (bottomEdge) size.height - edgeHeight else 0f
    )

    onDrawWithContent {
        drawContent()
        drawRect(
            topLeft = topLeft,
            size = Size(width = size.width, height = edgeHeight),
            brush = brush
        )
    }
}