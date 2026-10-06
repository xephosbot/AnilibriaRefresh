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
val AnilibertyIcons.Copyright: ImageVector
    get() {
        if (_copyright != null) {
            return _copyright!!
        }
        _copyright =
            Builder(
                    name = "Copyright",
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
                        moveTo(400.0f, 640.0f)
                        lineTo(560.0f, 640.0f)
                        quadTo(577.0f, 640.0f, 588.5f, 628.5f)
                        quadTo(600.0f, 617.0f, 600.0f, 600.0f)
                        lineTo(600.0f, 560.0f)
                        quadTo(600.0f, 543.0f, 588.5f, 531.5f)
                        quadTo(577.0f, 520.0f, 560.0f, 520.0f)
                        quadTo(543.0f, 520.0f, 531.5f, 531.5f)
                        quadTo(520.0f, 543.0f, 520.0f, 560.0f)
                        lineTo(440.0f, 560.0f)
                        lineTo(440.0f, 400.0f)
                        lineTo(520.0f, 400.0f)
                        quadTo(520.0f, 417.0f, 531.5f, 428.5f)
                        quadTo(543.0f, 440.0f, 560.0f, 440.0f)
                        quadTo(577.0f, 440.0f, 588.5f, 428.5f)
                        quadTo(600.0f, 417.0f, 600.0f, 400.0f)
                        lineTo(600.0f, 360.0f)
                        quadTo(600.0f, 343.0f, 588.5f, 331.5f)
                        quadTo(577.0f, 320.0f, 560.0f, 320.0f)
                        lineTo(400.0f, 320.0f)
                        quadTo(383.0f, 320.0f, 371.5f, 331.5f)
                        quadTo(360.0f, 343.0f, 360.0f, 360.0f)
                        lineTo(360.0f, 600.0f)
                        quadTo(360.0f, 617.0f, 371.5f, 628.5f)
                        quadTo(383.0f, 640.0f, 400.0f, 640.0f)
                        close()
                    }
                }
                .build()
        return _copyright!!
    }

private var _copyright: ImageVector? = null
