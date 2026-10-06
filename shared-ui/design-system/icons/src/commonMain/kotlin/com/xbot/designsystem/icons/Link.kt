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
val AnilibertyIcons.Link: ImageVector
    get() {
        if (_link != null) {
            return _link!!
        }
        _link =
            Builder(
                    name = "Link",
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
                        moveTo(280.0f, 680.0f)
                        quadTo(197.0f, 680.0f, 138.5f, 621.5f)
                        quadTo(80.0f, 563.0f, 80.0f, 480.0f)
                        quadTo(80.0f, 397.0f, 138.5f, 338.5f)
                        quadTo(197.0f, 280.0f, 280.0f, 280.0f)
                        lineTo(400.0f, 280.0f)
                        quadTo(417.0f, 280.0f, 428.5f, 291.5f)
                        quadTo(440.0f, 303.0f, 440.0f, 320.0f)
                        quadTo(440.0f, 337.0f, 428.5f, 348.5f)
                        quadTo(417.0f, 360.0f, 400.0f, 360.0f)
                        lineTo(280.0f, 360.0f)
                        quadTo(230.0f, 360.0f, 195.0f, 395.0f)
                        quadTo(160.0f, 430.0f, 160.0f, 480.0f)
                        quadTo(160.0f, 530.0f, 195.0f, 565.0f)
                        quadTo(230.0f, 600.0f, 280.0f, 600.0f)
                        lineTo(400.0f, 600.0f)
                        quadTo(417.0f, 600.0f, 428.5f, 611.5f)
                        quadTo(440.0f, 623.0f, 440.0f, 640.0f)
                        quadTo(440.0f, 657.0f, 428.5f, 668.5f)
                        quadTo(417.0f, 680.0f, 400.0f, 680.0f)
                        lineTo(280.0f, 680.0f)
                        close()
                        moveTo(360.0f, 520.0f)
                        quadTo(343.0f, 520.0f, 331.5f, 508.5f)
                        quadTo(320.0f, 497.0f, 320.0f, 480.0f)
                        quadTo(320.0f, 463.0f, 331.5f, 451.5f)
                        quadTo(343.0f, 440.0f, 360.0f, 440.0f)
                        lineTo(600.0f, 440.0f)
                        quadTo(617.0f, 440.0f, 628.5f, 451.5f)
                        quadTo(640.0f, 463.0f, 640.0f, 480.0f)
                        quadTo(640.0f, 497.0f, 628.5f, 508.5f)
                        quadTo(617.0f, 520.0f, 600.0f, 520.0f)
                        lineTo(360.0f, 520.0f)
                        close()
                        moveTo(560.0f, 680.0f)
                        quadTo(543.0f, 680.0f, 531.5f, 668.5f)
                        quadTo(520.0f, 657.0f, 520.0f, 640.0f)
                        quadTo(520.0f, 623.0f, 531.5f, 611.5f)
                        quadTo(543.0f, 600.0f, 560.0f, 600.0f)
                        lineTo(680.0f, 600.0f)
                        quadTo(730.0f, 600.0f, 765.0f, 565.0f)
                        quadTo(800.0f, 530.0f, 800.0f, 480.0f)
                        quadTo(800.0f, 430.0f, 765.0f, 395.0f)
                        quadTo(730.0f, 360.0f, 680.0f, 360.0f)
                        lineTo(560.0f, 360.0f)
                        quadTo(543.0f, 360.0f, 531.5f, 348.5f)
                        quadTo(520.0f, 337.0f, 520.0f, 320.0f)
                        quadTo(520.0f, 303.0f, 531.5f, 291.5f)
                        quadTo(543.0f, 280.0f, 560.0f, 280.0f)
                        lineTo(680.0f, 280.0f)
                        quadTo(763.0f, 280.0f, 821.5f, 338.5f)
                        quadTo(880.0f, 397.0f, 880.0f, 480.0f)
                        quadTo(880.0f, 563.0f, 821.5f, 621.5f)
                        quadTo(763.0f, 680.0f, 680.0f, 680.0f)
                        lineTo(560.0f, 680.0f)
                        close()
                    }
                }
                .build()
        return _link!!
    }

private var _link: ImageVector? = null
