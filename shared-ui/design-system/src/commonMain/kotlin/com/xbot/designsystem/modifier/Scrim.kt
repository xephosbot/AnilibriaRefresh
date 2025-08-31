package com.xbot.designsystem.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.scrim(
    edgeHeightRatio: Float = 0.5f,
    opacity: Float = 0.8f,
    bottomEdge: Boolean = true,
): Modifier = this then drawWithCache {
    val edgeHeightPx = size.height * edgeHeightRatio
    val brush = Brush.verticalGradient(
        colors = listOf(Color.Black.copy(alpha = opacity), Color.Transparent),
        startY = if (bottomEdge) size.height else 0f,
        endY = if (bottomEdge) size.height - edgeHeightPx else edgeHeightPx,
    )
    onDrawWithContent {
        drawContent()
        drawRect(
            topLeft = Offset(0f, if (bottomEdge) size.height - edgeHeightPx else 0f),
            size = Size(size.width, edgeHeightPx),
            brush = brush
        )
    }
}