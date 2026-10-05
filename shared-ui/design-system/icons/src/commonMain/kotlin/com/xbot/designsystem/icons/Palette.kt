package com.xbot.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("UnusedReceiverParameter")
val AnilibertyIcons.Filled.Palette: ImageVector
    get() {
        if (_paletteFilled != null) {
            return _paletteFilled!!
        }
        _paletteFilled =
            Builder(
                    name = "PaletteFilled",
                    defaultWidth = 24.0.dp,
                    defaultHeight = 24.0.dp,
                    viewportWidth = 960.0f,
                    viewportHeight = 960.0f,
                )
                .apply {
                    path(
                        fill = SolidColor(Color(0xFF000000)),
                        stroke = null,
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = NonZero,
                    ) {
                        moveTo(480.0f, 880.0f)
                        quadTo(398.0f, 880.0f, 325.0f, 848.5f)
                        quadTo(252.0f, 817.0f, 197.5f, 762.5f)
                        quadTo(143.0f, 708.0f, 111.5f, 635.0f)
                        quadTo(80.0f, 562.0f, 80.0f, 480.0f)
                        quadTo(80.0f, 397.0f, 112.5f, 324.0f)
                        quadTo(145.0f, 251.0f, 200.5f, 197.0f)
                        quadTo(256.0f, 143.0f, 330.0f, 111.5f)
                        quadTo(404.0f, 80.0f, 488.0f, 80.0f)
                        quadTo(568.0f, 80.0f, 639.0f, 107.5f)
                        quadTo(710.0f, 135.0f, 763.5f, 183.5f)
                        quadTo(817.0f, 232.0f, 848.5f, 298.5f)
                        quadTo(880.0f, 365.0f, 880.0f, 442.0f)
                        quadTo(880.0f, 557.0f, 810.0f, 618.5f)
                        quadTo(740.0f, 680.0f, 640.0f, 680.0f)
                        lineTo(566.0f, 680.0f)
                        quadTo(557.0f, 680.0f, 553.5f, 685.0f)
                        quadTo(550.0f, 690.0f, 550.0f, 696.0f)
                        quadTo(550.0f, 708.0f, 565.0f, 730.5f)
                        quadTo(580.0f, 753.0f, 580.0f, 782.0f)
                        quadTo(580.0f, 832.0f, 552.5f, 856.0f)
                        quadTo(525.0f, 880.0f, 480.0f, 880.0f)
                        close()
                        moveTo(260.0f, 520.0f)
                        quadTo(286.0f, 520.0f, 303.0f, 503.0f)
                        quadTo(320.0f, 486.0f, 320.0f, 460.0f)
                        quadTo(320.0f, 434.0f, 303.0f, 417.0f)
                        quadTo(286.0f, 400.0f, 260.0f, 400.0f)
                        quadTo(234.0f, 400.0f, 217.0f, 417.0f)
                        quadTo(200.0f, 434.0f, 200.0f, 460.0f)
                        quadTo(200.0f, 486.0f, 217.0f, 503.0f)
                        quadTo(234.0f, 520.0f, 260.0f, 520.0f)
                        close()
                        moveTo(380.0f, 360.0f)
                        quadTo(406.0f, 360.0f, 423.0f, 343.0f)
                        quadTo(440.0f, 326.0f, 440.0f, 300.0f)
                        quadTo(440.0f, 274.0f, 423.0f, 257.0f)
                        quadTo(406.0f, 240.0f, 380.0f, 240.0f)
                        quadTo(354.0f, 240.0f, 337.0f, 257.0f)
                        quadTo(320.0f, 274.0f, 320.0f, 300.0f)
                        quadTo(320.0f, 326.0f, 337.0f, 343.0f)
                        quadTo(354.0f, 360.0f, 380.0f, 360.0f)
                        close()
                        moveTo(580.0f, 360.0f)
                        quadTo(606.0f, 360.0f, 623.0f, 343.0f)
                        quadTo(640.0f, 326.0f, 640.0f, 300.0f)
                        quadTo(640.0f, 274.0f, 623.0f, 257.0f)
                        quadTo(606.0f, 240.0f, 580.0f, 240.0f)
                        quadTo(554.0f, 240.0f, 537.0f, 257.0f)
                        quadTo(520.0f, 274.0f, 520.0f, 300.0f)
                        quadTo(520.0f, 326.0f, 537.0f, 343.0f)
                        quadTo(554.0f, 360.0f, 580.0f, 360.0f)
                        close()
                        moveTo(700.0f, 520.0f)
                        quadTo(726.0f, 520.0f, 743.0f, 503.0f)
                        quadTo(760.0f, 486.0f, 760.0f, 460.0f)
                        quadTo(760.0f, 434.0f, 743.0f, 417.0f)
                        quadTo(726.0f, 400.0f, 700.0f, 400.0f)
                        quadTo(674.0f, 400.0f, 657.0f, 417.0f)
                        quadTo(640.0f, 434.0f, 640.0f, 460.0f)
                        quadTo(640.0f, 486.0f, 657.0f, 503.0f)
                        quadTo(674.0f, 520.0f, 700.0f, 520.0f)
                        close()
                    }
                }
                .build()
        return _paletteFilled!!
    }

private var _paletteFilled: ImageVector? = null
