package com.xbot.anilibriarefresh.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.drawFadingEdge(
    edgeHeight: Dp = DefaultFadingEdgeHeight
) = then(
    Modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, Color.Black, Color.Transparent),
                    startY = size.height - edgeHeight.toPx(),
                    endY = size.height
                ),
                blendMode = BlendMode.DstIn,
            )
        }
)

private val DefaultFadingEdgeHeight = 96.dp