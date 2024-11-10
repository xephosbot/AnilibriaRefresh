package com.xbot.anilibriarefresh.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.fadedEdge(
    bottomEdge: Boolean = true,
    edgeHeight: Dp = DefaultFadingEdgeHeight,
) = graphicsLayer {
    compositingStrategy = CompositingStrategy.Offscreen
}.drawWithContent {
    drawContent()
    drawFadedEdge(bottomEdge, edgeHeight)
}

private fun ContentDrawScope.drawFadedEdge(bottomEdge: Boolean, edgeHeight: Dp) {
    val edgeHeightPx = edgeHeight.toPx()
    drawRect(
        topLeft = Offset(0f, if (bottomEdge) size.height - edgeHeightPx else 0f),
        size = Size(size.width, edgeHeightPx),
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startY = if (bottomEdge) size.height else 0f,
            endY = if (bottomEdge) size.height - edgeHeightPx else edgeHeightPx,
        ),
        blendMode = BlendMode.DstIn,
    )
}

private val DefaultFadingEdgeHeight = 96.dp
