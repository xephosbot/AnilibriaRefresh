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
val AnilibertyIcons.ArrowForward: ImageVector
    get() {
        if (_arrowForward != null) {
            return _arrowForward!!
        }
        _arrowForward =
            Builder(
                    name = "ArrowForward",
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
                        moveTo(647.0f, 520.0f)
                        lineTo(200.0f, 520.0f)
                        quadTo(183.0f, 520.0f, 171.5f, 508.5f)
                        quadTo(160.0f, 497.0f, 160.0f, 480.0f)
                        quadTo(160.0f, 463.0f, 171.5f, 451.5f)
                        quadTo(183.0f, 440.0f, 200.0f, 440.0f)
                        lineTo(647.0f, 440.0f)
                        lineTo(451.0f, 244.0f)
                        quadTo(439.0f, 232.0f, 439.5f, 216.0f)
                        quadTo(440.0f, 200.0f, 452.0f, 188.0f)
                        quadTo(464.0f, 177.0f, 480.0f, 176.5f)
                        quadTo(496.0f, 176.0f, 508.0f, 188.0f)
                        lineTo(772.0f, 452.0f)
                        quadTo(778.0f, 458.0f, 780.5f, 465.0f)
                        quadTo(783.0f, 472.0f, 783.0f, 480.0f)
                        quadTo(783.0f, 488.0f, 780.5f, 495.0f)
                        quadTo(778.0f, 502.0f, 772.0f, 508.0f)
                        lineTo(508.0f, 772.0f)
                        quadTo(497.0f, 783.0f, 480.5f, 783.0f)
                        quadTo(464.0f, 783.0f, 452.0f, 772.0f)
                        quadTo(440.0f, 760.0f, 440.0f, 743.5f)
                        quadTo(440.0f, 727.0f, 452.0f, 715.0f)
                        lineTo(647.0f, 520.0f)
                        close()
                    }
                }
                .build()
        return _arrowForward!!
    }

private var _arrowForward: ImageVector? = null
