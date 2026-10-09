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
val AnilibertyIcons.Error: ImageVector
    get() {
        if (_error != null) {
            return _error!!
        }
        _error =
            Builder(
                name = "Error",
                defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp,
                viewportWidth = 960.0f,
                viewportHeight = 960.0f
            )
                .apply {
                    path(
                        fill = SolidColor(Color(0xFF000000)),
                        stroke = null,
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = NonZero
                    ) {
                        moveTo(480.0f, 680.0f)
                        quadTo(497.0f, 680.0f, 508.5f, 668.5f)
                        quadTo(520.0f, 657.0f, 520.0f, 640.0f)
                        quadTo(520.0f, 623.0f, 508.5f, 611.5f)
                        quadTo(497.0f, 600.0f, 480.0f, 600.0f)
                        quadTo(463.0f, 600.0f, 451.5f, 611.5f)
                        quadTo(440.0f, 623.0f, 440.0f, 640.0f)
                        quadTo(440.0f, 657.0f, 451.5f, 668.5f)
                        quadTo(463.0f, 680.0f, 480.0f, 680.0f)
                        close()
                        moveTo(480.0f, 520.0f)
                        quadTo(497.0f, 520.0f, 508.5f, 508.5f)
                        quadTo(520.0f, 497.0f, 520.0f, 480.0f)
                        lineTo(520.0f, 320.0f)
                        quadTo(520.0f, 303.0f, 508.5f, 291.5f)
                        quadTo(497.0f, 280.0f, 480.0f, 280.0f)
                        quadTo(463.0f, 280.0f, 451.5f, 291.5f)
                        quadTo(440.0f, 303.0f, 440.0f, 320.0f)
                        lineTo(440.0f, 480.0f)
                        quadTo(440.0f, 497.0f, 451.5f, 508.5f)
                        quadTo(463.0f, 520.0f, 480.0f, 520.0f)
                        close()
                        moveTo(480.0f, 880.0f)
                        quadTo(397.0f, 880.0f, 324.0f, 848.5f)
                        quadTo(251.0f, 817.0f, 197.0f, 763.0f)
                        quadTo(143.0f, 709.0f, 111.5f, 636.0f)
                        quadTo(80.0f, 563.0f, 80.0f, 480.0f)
                        quadTo(80.0f, 397.0f, 111.5f, 324.0f)
                        quadTo(143.0f, 251.0f, 197.0f, 197.0f)
                        quadTo(251.0f, 143.0f, 324.0f, 111.5f)
                        quadTo(397.0f, 80.0f, 480.0f, 80.0f)
                        quadTo(563.0f, 80.0f, 636.0f, 111.5f)
                        quadTo(709.0f, 143.0f, 763.0f, 197.0f)
                        quadTo(817.0f, 251.0f, 848.5f, 324.0f)
                        quadTo(880.0f, 397.0f, 880.0f, 480.0f)
                        quadTo(880.0f, 563.0f, 848.5f, 636.0f)
                        quadTo(817.0f, 709.0f, 763.0f, 763.0f)
                        quadTo(709.0f, 817.0f, 636.0f, 848.5f)
                        quadTo(563.0f, 880.0f, 480.0f, 880.0f)
                        close()
                        moveTo(480.0f, 800.0f)
                        quadTo(614.0f, 800.0f, 707.0f, 707.0f)
                        quadTo(800.0f, 614.0f, 800.0f, 480.0f)
                        quadTo(800.0f, 346.0f, 707.0f, 253.0f)
                        quadTo(614.0f, 160.0f, 480.0f, 160.0f)
                        quadTo(346.0f, 160.0f, 253.0f, 253.0f)
                        quadTo(160.0f, 346.0f, 160.0f, 480.0f)
                        quadTo(160.0f, 614.0f, 253.0f, 707.0f)
                        quadTo(346.0f, 800.0f, 480.0f, 800.0f)
                        close()
                        moveTo(480.0f, 480.0f)
                        close()
                    }
                }
                .build()
        return _error!!
    }

private var _error: ImageVector? = null
