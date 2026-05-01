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
val AnilibriaIcons.Share: ImageVector
    get() {
        if (_share != null) {
            return _share!!
        }
        _share =
            Builder(
                    name = "Share",
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
                        moveTo(680.0f, 880.0f)
                        quadTo(630.0f, 880.0f, 595.0f, 845.0f)
                        quadTo(560.0f, 810.0f, 560.0f, 760.0f)
                        quadTo(560.0f, 754.0f, 563.0f, 732.0f)
                        lineTo(282.0f, 568.0f)
                        quadTo(266.0f, 583.0f, 245.0f, 591.5f)
                        quadTo(224.0f, 600.0f, 200.0f, 600.0f)
                        quadTo(150.0f, 600.0f, 115.0f, 565.0f)
                        quadTo(80.0f, 530.0f, 80.0f, 480.0f)
                        quadTo(80.0f, 430.0f, 115.0f, 395.0f)
                        quadTo(150.0f, 360.0f, 200.0f, 360.0f)
                        quadTo(224.0f, 360.0f, 245.0f, 368.5f)
                        quadTo(266.0f, 377.0f, 282.0f, 392.0f)
                        lineTo(563.0f, 228.0f)
                        quadTo(561.0f, 221.0f, 560.5f, 214.5f)
                        quadTo(560.0f, 208.0f, 560.0f, 200.0f)
                        quadTo(560.0f, 150.0f, 595.0f, 115.0f)
                        quadTo(630.0f, 80.0f, 680.0f, 80.0f)
                        quadTo(730.0f, 80.0f, 765.0f, 115.0f)
                        quadTo(800.0f, 150.0f, 800.0f, 200.0f)
                        quadTo(800.0f, 250.0f, 765.0f, 285.0f)
                        quadTo(730.0f, 320.0f, 680.0f, 320.0f)
                        quadTo(656.0f, 320.0f, 635.0f, 311.5f)
                        quadTo(614.0f, 303.0f, 598.0f, 288.0f)
                        lineTo(317.0f, 452.0f)
                        quadTo(319.0f, 459.0f, 319.5f, 465.5f)
                        quadTo(320.0f, 472.0f, 320.0f, 480.0f)
                        quadTo(320.0f, 488.0f, 319.5f, 494.5f)
                        quadTo(319.0f, 501.0f, 317.0f, 508.0f)
                        lineTo(598.0f, 672.0f)
                        quadTo(614.0f, 657.0f, 635.0f, 648.5f)
                        quadTo(656.0f, 640.0f, 680.0f, 640.0f)
                        quadTo(730.0f, 640.0f, 765.0f, 675.0f)
                        quadTo(800.0f, 710.0f, 800.0f, 760.0f)
                        quadTo(800.0f, 810.0f, 765.0f, 845.0f)
                        quadTo(730.0f, 880.0f, 680.0f, 880.0f)
                        close()
                    }
                }
                .build()
        return _share!!
    }

private var _share: ImageVector? = null
