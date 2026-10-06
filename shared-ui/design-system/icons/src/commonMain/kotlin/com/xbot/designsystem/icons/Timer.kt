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
val AnilibertyIcons.Timer: ImageVector
    get() {
        if (_timer != null) {
            return _timer!!
        }
        _timer =
            Builder(
                name = "Timer",
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
                        moveTo(400.0f, 120.0f)
                        quadTo(383.0f, 120.0f, 371.5f, 108.5f)
                        quadTo(360.0f, 97.0f, 360.0f, 80.0f)
                        quadTo(360.0f, 63.0f, 371.5f, 51.5f)
                        quadTo(383.0f, 40.0f, 400.0f, 40.0f)
                        lineTo(560.0f, 40.0f)
                        quadTo(577.0f, 40.0f, 588.5f, 51.5f)
                        quadTo(600.0f, 63.0f, 600.0f, 80.0f)
                        quadTo(600.0f, 97.0f, 588.5f, 108.5f)
                        quadTo(577.0f, 120.0f, 560.0f, 120.0f)
                        lineTo(400.0f, 120.0f)
                        close()
                        moveTo(480.0f, 560.0f)
                        quadTo(497.0f, 560.0f, 508.5f, 548.5f)
                        quadTo(520.0f, 537.0f, 520.0f, 520.0f)
                        lineTo(520.0f, 360.0f)
                        quadTo(520.0f, 343.0f, 508.5f, 331.5f)
                        quadTo(497.0f, 320.0f, 480.0f, 320.0f)
                        quadTo(463.0f, 320.0f, 451.5f, 331.5f)
                        quadTo(440.0f, 343.0f, 440.0f, 360.0f)
                        lineTo(440.0f, 520.0f)
                        quadTo(440.0f, 537.0f, 451.5f, 548.5f)
                        quadTo(463.0f, 560.0f, 480.0f, 560.0f)
                        close()
                        moveTo(480.0f, 880.0f)
                        quadTo(406.0f, 880.0f, 340.5f, 851.5f)
                        quadTo(275.0f, 823.0f, 226.0f, 774.0f)
                        quadTo(177.0f, 725.0f, 148.5f, 659.5f)
                        quadTo(120.0f, 594.0f, 120.0f, 520.0f)
                        quadTo(120.0f, 446.0f, 148.5f, 380.5f)
                        quadTo(177.0f, 315.0f, 226.0f, 266.0f)
                        quadTo(275.0f, 217.0f, 340.5f, 188.5f)
                        quadTo(406.0f, 160.0f, 480.0f, 160.0f)
                        quadTo(542.0f, 160.0f, 599.0f, 180.0f)
                        quadTo(656.0f, 200.0f, 706.0f, 238.0f)
                        lineTo(734.0f, 210.0f)
                        quadTo(745.0f, 199.0f, 762.0f, 199.0f)
                        quadTo(779.0f, 199.0f, 790.0f, 210.0f)
                        quadTo(801.0f, 221.0f, 801.0f, 238.0f)
                        quadTo(801.0f, 255.0f, 790.0f, 266.0f)
                        lineTo(762.0f, 294.0f)
                        quadTo(800.0f, 344.0f, 820.0f, 401.0f)
                        quadTo(840.0f, 458.0f, 840.0f, 520.0f)
                        quadTo(840.0f, 594.0f, 811.5f, 659.5f)
                        quadTo(783.0f, 725.0f, 734.0f, 774.0f)
                        quadTo(685.0f, 823.0f, 619.5f, 851.5f)
                        quadTo(554.0f, 880.0f, 480.0f, 880.0f)
                        close()
                        moveTo(480.0f, 800.0f)
                        quadTo(596.0f, 800.0f, 678.0f, 718.0f)
                        quadTo(760.0f, 636.0f, 760.0f, 520.0f)
                        quadTo(760.0f, 404.0f, 678.0f, 322.0f)
                        quadTo(596.0f, 240.0f, 480.0f, 240.0f)
                        quadTo(364.0f, 240.0f, 282.0f, 322.0f)
                        quadTo(200.0f, 404.0f, 200.0f, 520.0f)
                        quadTo(200.0f, 636.0f, 282.0f, 718.0f)
                        quadTo(364.0f, 800.0f, 480.0f, 800.0f)
                        close()
                        moveTo(480.0f, 520.0f)
                        close()
                    }
                }
                .build()
        return _timer!!
    }

private var _timer: ImageVector? = null
