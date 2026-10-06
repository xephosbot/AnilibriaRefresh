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
val AnilibertyIcons.SmartDisplay: ImageVector
    get() {
        if (_smartDisplay != null) {
            return _smartDisplay!!
        }
        _smartDisplay =
            Builder(
                name = "SmartDisplay",
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
                        moveTo(411.0f, 640.0f)
                        lineTo(634.0f, 497.0f)
                        quadTo(643.0f, 491.0f, 643.0f, 480.0f)
                        quadTo(643.0f, 469.0f, 634.0f, 463.0f)
                        lineTo(411.0f, 320.0f)
                        quadTo(401.0f, 313.0f, 390.5f, 319.0f)
                        quadTo(380.0f, 325.0f, 380.0f, 337.0f)
                        lineTo(380.0f, 623.0f)
                        quadTo(380.0f, 635.0f, 390.5f, 641.0f)
                        quadTo(401.0f, 647.0f, 411.0f, 640.0f)
                        close()
                        moveTo(160.0f, 800.0f)
                        quadTo(127.0f, 800.0f, 103.5f, 776.5f)
                        quadTo(80.0f, 753.0f, 80.0f, 720.0f)
                        lineTo(80.0f, 240.0f)
                        quadTo(80.0f, 207.0f, 103.5f, 183.5f)
                        quadTo(127.0f, 160.0f, 160.0f, 160.0f)
                        lineTo(800.0f, 160.0f)
                        quadTo(833.0f, 160.0f, 856.5f, 183.5f)
                        quadTo(880.0f, 207.0f, 880.0f, 240.0f)
                        lineTo(880.0f, 720.0f)
                        quadTo(880.0f, 753.0f, 856.5f, 776.5f)
                        quadTo(833.0f, 800.0f, 800.0f, 800.0f)
                        lineTo(160.0f, 800.0f)
                        close()
                        moveTo(160.0f, 720.0f)
                        lineTo(800.0f, 720.0f)
                        lineTo(800.0f, 240.0f)
                        lineTo(160.0f, 240.0f)
                        lineTo(160.0f, 720.0f)
                        close()
                        moveTo(160.0f, 720.0f)
                        lineTo(160.0f, 240.0f)
                        lineTo(160.0f, 720.0f)
                        close()
                    }
                }
                .build()
        return _smartDisplay!!
    }

private var _smartDisplay: ImageVector? = null
