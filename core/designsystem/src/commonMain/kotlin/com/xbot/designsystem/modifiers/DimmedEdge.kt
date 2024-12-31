package com.xbot.designsystem.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.dimmedEdge(
    edgeHeightRatio: Float = 0.5f,
    bottomEdge: Boolean = true,
) = drawWithCache {
    val edgeHeightPx = size.width * edgeHeightRatio
    val brush = Brush.verticalGradient(
        colors = listOf(Color.Black, Color.Transparent),
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