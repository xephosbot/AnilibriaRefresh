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
val AnilibertyIcons.ContentCopy: ImageVector
    get() {
        if (_contentCopy != null) {
            return _contentCopy!!
        }
        _contentCopy =
            Builder(
                name = "ContentCopy",
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
                        moveTo(360.0f, 720.0f)
                        quadTo(327.0f, 720.0f, 303.5f, 696.5f)
                        quadTo(280.0f, 673.0f, 280.0f, 640.0f)
                        lineTo(280.0f, 160.0f)
                        quadTo(280.0f, 127.0f, 303.5f, 103.5f)
                        quadTo(327.0f, 80.0f, 360.0f, 80.0f)
                        lineTo(720.0f, 80.0f)
                        quadTo(753.0f, 80.0f, 776.5f, 103.5f)
                        quadTo(800.0f, 127.0f, 800.0f, 160.0f)
                        lineTo(800.0f, 640.0f)
                        quadTo(800.0f, 673.0f, 776.5f, 696.5f)
                        quadTo(753.0f, 720.0f, 720.0f, 720.0f)
                        lineTo(360.0f, 720.0f)
                        close()
                        moveTo(360.0f, 640.0f)
                        lineTo(720.0f, 640.0f)
                        lineTo(720.0f, 160.0f)
                        lineTo(360.0f, 160.0f)
                        lineTo(360.0f, 640.0f)
                        close()
                        moveTo(200.0f, 880.0f)
                        quadTo(167.0f, 880.0f, 143.5f, 856.5f)
                        quadTo(120.0f, 833.0f, 120.0f, 800.0f)
                        lineTo(120.0f, 280.0f)
                        quadTo(120.0f, 263.0f, 131.5f, 251.5f)
                        quadTo(143.0f, 240.0f, 160.0f, 240.0f)
                        quadTo(177.0f, 240.0f, 188.5f, 251.5f)
                        quadTo(200.0f, 263.0f, 200.0f, 280.0f)
                        lineTo(200.0f, 800.0f)
                        lineTo(600.0f, 800.0f)
                        quadTo(617.0f, 800.0f, 628.5f, 811.5f)
                        quadTo(640.0f, 823.0f, 640.0f, 840.0f)
                        quadTo(640.0f, 857.0f, 628.5f, 868.5f)
                        quadTo(617.0f, 880.0f, 600.0f, 880.0f)
                        lineTo(200.0f, 880.0f)
                        close()
                        moveTo(360.0f, 640.0f)
                        lineTo(360.0f, 160.0f)
                        lineTo(360.0f, 640.0f)
                        close()
                    }
                }
                .build()
        return _contentCopy!!
    }

private var _contentCopy: ImageVector? = null
