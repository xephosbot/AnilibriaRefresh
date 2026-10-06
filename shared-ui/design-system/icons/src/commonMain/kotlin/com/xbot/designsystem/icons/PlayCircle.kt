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
val AnilibertyIcons.PlayCircle: ImageVector
    get() {
        if (_playCircle != null) {
            return _playCircle!!
        }
        _playCircle =
            Builder(
                    name = "PlayCircle",
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
                        moveTo(426.0f, 630.0f)
                        lineTo(621.0f, 505.0f)
                        quadTo(635.0f, 496.0f, 635.0f, 480.0f)
                        quadTo(635.0f, 464.0f, 621.0f, 455.0f)
                        lineTo(426.0f, 330.0f)
                        quadTo(411.0f, 320.0f, 395.5f, 328.5f)
                        quadTo(380.0f, 337.0f, 380.0f, 355.0f)
                        lineTo(380.0f, 605.0f)
                        quadTo(380.0f, 623.0f, 395.5f, 631.5f)
                        quadTo(411.0f, 640.0f, 426.0f, 630.0f)
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
        return _playCircle!!
    }

private var _playCircle: ImageVector? = null
