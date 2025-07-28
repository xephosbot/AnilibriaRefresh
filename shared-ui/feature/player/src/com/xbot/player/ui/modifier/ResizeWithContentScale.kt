package com.xbot.player.ui.modifier

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

/**
 * Attempts to size the original content rectangle to be inscribed into a destination by applying a
 * specified [ContentScale] type.
 */
@Composable
fun Modifier.resizeWithContentScale(
    contentScale: ContentScale,
    sourceSizeDp: Size?,
    density: Density = LocalDensity.current,
): Modifier =
    then(
        Modifier.fillMaxSize()
            .wrapContentSize()
            .then(
                sourceSizeDp?.let { srcSizeDp ->
                    Modifier.layout { measurable, constraints ->
                        val srcSizePx =
                            with(density) { Size(Dp(srcSizeDp.width).toPx(), Dp(srcSizeDp.height).toPx()) }
                        val dstSizePx = Size(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())
                        val scaleFactor = contentScale.computeScaleFactor(srcSizePx, dstSizePx)
                        val placeable =
                            measurable.measure(
                                constraints.copy(
                                    maxWidth = (srcSizePx.width * scaleFactor.scaleX).roundToInt(),
                                    maxHeight = (srcSizePx.height * scaleFactor.scaleY).roundToInt(),
                                )
                            )
                        layout(placeable.width, placeable.height) { placeable.place(0, 0) }
                    }
                } ?: Modifier
            )
    )