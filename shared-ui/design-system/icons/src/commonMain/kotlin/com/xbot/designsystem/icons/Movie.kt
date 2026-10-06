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
val AnilibertyIcons.Movie: ImageVector
    get() {
        if (_movie != null) {
            return _movie!!
        }
        _movie =
            Builder(
                    name = "Movie",
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
                        moveTo(160.0f, 160.0f)
                        lineTo(225.0f, 290.0f)
                        quadTo(232.0f, 304.0f, 245.0f, 312.0f)
                        quadTo(258.0f, 320.0f, 273.0f, 320.0f)
                        quadTo(303.0f, 320.0f, 319.0f, 294.5f)
                        quadTo(335.0f, 269.0f, 321.0f, 242.0f)
                        lineTo(280.0f, 160.0f)
                        lineTo(360.0f, 160.0f)
                        lineTo(425.0f, 290.0f)
                        quadTo(432.0f, 304.0f, 445.0f, 312.0f)
                        quadTo(458.0f, 320.0f, 473.0f, 320.0f)
                        quadTo(503.0f, 320.0f, 519.0f, 294.5f)
                        quadTo(535.0f, 269.0f, 521.0f, 242.0f)
                        lineTo(480.0f, 160.0f)
                        lineTo(560.0f, 160.0f)
                        lineTo(625.0f, 290.0f)
                        quadTo(632.0f, 304.0f, 645.0f, 312.0f)
                        quadTo(658.0f, 320.0f, 673.0f, 320.0f)
                        quadTo(703.0f, 320.0f, 719.0f, 294.5f)
                        quadTo(735.0f, 269.0f, 721.0f, 242.0f)
                        lineTo(680.0f, 160.0f)
                        lineTo(800.0f, 160.0f)
                        quadTo(833.0f, 160.0f, 856.5f, 183.5f)
                        quadTo(880.0f, 207.0f, 880.0f, 240.0f)
                        lineTo(880.0f, 720.0f)
                        quadTo(880.0f, 753.0f, 856.5f, 776.5f)
                        quadTo(833.0f, 800.0f, 800.0f, 800.0f)
                        lineTo(160.0f, 800.0f)
                        quadTo(127.0f, 800.0f, 103.5f, 776.5f)
                        quadTo(80.0f, 753.0f, 80.0f, 720.0f)
                        lineTo(80.0f, 240.0f)
                        quadTo(80.0f, 207.0f, 103.5f, 183.5f)
                        quadTo(127.0f, 160.0f, 160.0f, 160.0f)
                        close()
                        moveTo(160.0f, 400.0f)
                        lineTo(160.0f, 720.0f)
                        lineTo(800.0f, 720.0f)
                        lineTo(800.0f, 400.0f)
                        lineTo(160.0f, 400.0f)
                        close()
                        moveTo(160.0f, 400.0f)
                        lineTo(160.0f, 720.0f)
                        lineTo(160.0f, 400.0f)
                        close()
                    }
                }
                .build()
        return _movie!!
    }

private var _movie: ImageVector? = null
