package com.xbot.designsystem.modifier

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.DpOffset
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun Modifier.overlayDrawable(
    resource: DrawableResource,
    brush: Brush = Brush.verticalGradient(
        colors = listOf(
            Color.White,
            MaterialTheme.colorScheme.primaryContainer,
        ),
        startY = 0.0f,
        endY = 250.0f
    ),
    offset: DpOffset = DpOffset.Zero
): Modifier {
    val painter = painterResource(resource)

    return this.then(
        drawWithCache {
            val painterSize = if (painter.intrinsicSize.isSpecified) {
                painter.intrinsicSize
            } else {
                size
            }
            val layerBounds = Rect(
                offset.x.toPx(), offset.y.toPx(),
                offset.x.toPx() + painterSize.width,
                offset.y.toPx() + painterSize.height
            )

            onDrawWithContent {
                drawContent()

                drawIntoCanvas { canvas ->
                    canvas.saveLayer(layerBounds, Paint())

                    translate(offset.x.toPx(), offset.y.toPx()) {
                        with(painter) {
                            draw(size = painterSize)
                        }

                        drawRect(
                            brush = brush,
                            size = painterSize,
                            blendMode = BlendMode.SrcAtop
                        )
                    }

                    canvas.restore()
                }
            }
        }
    )
}