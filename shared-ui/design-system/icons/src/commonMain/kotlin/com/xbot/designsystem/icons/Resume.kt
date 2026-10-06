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
val AnilibertyIcons.Resume: ImageVector
    get() {
        if (_resume != null) {
            return _resume!!
        }
        _resume =
            Builder(
                name = "Resume",
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
                        moveTo(240.0f, 680.0f)
                        lineTo(240.0f, 280.0f)
                        quadTo(240.0f, 263.0f, 251.5f, 251.5f)
                        quadTo(263.0f, 240.0f, 280.0f, 240.0f)
                        quadTo(297.0f, 240.0f, 308.5f, 251.5f)
                        quadTo(320.0f, 263.0f, 320.0f, 280.0f)
                        lineTo(320.0f, 680.0f)
                        quadTo(320.0f, 697.0f, 308.5f, 708.5f)
                        quadTo(297.0f, 720.0f, 280.0f, 720.0f)
                        quadTo(263.0f, 720.0f, 251.5f, 708.5f)
                        quadTo(240.0f, 697.0f, 240.0f, 680.0f)
                        close()
                        moveTo(461.0f, 684.0f)
                        lineTo(743.0f, 514.0f)
                        quadTo(753.0f, 508.0f, 757.5f, 499.0f)
                        quadTo(762.0f, 490.0f, 762.0f, 480.0f)
                        quadTo(762.0f, 470.0f, 757.5f, 461.0f)
                        quadTo(753.0f, 452.0f, 743.0f, 446.0f)
                        lineTo(461.0f, 276.0f)
                        quadTo(456.0f, 273.0f, 450.5f, 272.0f)
                        quadTo(445.0f, 271.0f, 440.0f, 271.0f)
                        quadTo(424.0f, 271.0f, 412.0f, 282.5f)
                        quadTo(400.0f, 294.0f, 400.0f, 311.0f)
                        lineTo(400.0f, 649.0f)
                        quadTo(400.0f, 666.0f, 412.0f, 677.5f)
                        quadTo(424.0f, 689.0f, 440.0f, 689.0f)
                        quadTo(445.0f, 689.0f, 450.5f, 688.0f)
                        quadTo(456.0f, 687.0f, 461.0f, 684.0f)
                        close()
                        moveTo(480.0f, 579.0f)
                        lineTo(480.0f, 381.0f)
                        lineTo(645.0f, 480.0f)
                        lineTo(480.0f, 579.0f)
                        close()
                        moveTo(480.0f, 480.0f)
                        close()
                    }
                }
                .build()
        return _resume!!
    }

private var _resume: ImageVector? = null
