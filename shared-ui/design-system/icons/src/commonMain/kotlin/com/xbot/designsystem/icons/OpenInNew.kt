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
val AnilibertyIcons.OpenInNew: ImageVector
    get() {
        if (_openInNew != null) {
            return _openInNew!!
        }
        _openInNew =
            Builder(
                name = "OpenInNew",
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
                        moveTo(200.0f, 840.0f)
                        quadTo(167.0f, 840.0f, 143.5f, 816.5f)
                        quadTo(120.0f, 793.0f, 120.0f, 760.0f)
                        lineTo(120.0f, 200.0f)
                        quadTo(120.0f, 167.0f, 143.5f, 143.5f)
                        quadTo(167.0f, 120.0f, 200.0f, 120.0f)
                        lineTo(440.0f, 120.0f)
                        quadTo(457.0f, 120.0f, 468.5f, 131.5f)
                        quadTo(480.0f, 143.0f, 480.0f, 160.0f)
                        quadTo(480.0f, 177.0f, 468.5f, 188.5f)
                        quadTo(457.0f, 200.0f, 440.0f, 200.0f)
                        lineTo(200.0f, 200.0f)
                        lineTo(200.0f, 760.0f)
                        lineTo(760.0f, 760.0f)
                        lineTo(760.0f, 520.0f)
                        quadTo(760.0f, 503.0f, 771.5f, 491.5f)
                        quadTo(783.0f, 480.0f, 800.0f, 480.0f)
                        quadTo(817.0f, 480.0f, 828.5f, 491.5f)
                        quadTo(840.0f, 503.0f, 840.0f, 520.0f)
                        lineTo(840.0f, 760.0f)
                        quadTo(840.0f, 793.0f, 816.5f, 816.5f)
                        quadTo(793.0f, 840.0f, 760.0f, 840.0f)
                        lineTo(200.0f, 840.0f)
                        close()
                        moveTo(760.0f, 256.0f)
                        lineTo(416.0f, 600.0f)
                        quadTo(405.0f, 611.0f, 388.0f, 611.0f)
                        quadTo(371.0f, 611.0f, 360.0f, 600.0f)
                        quadTo(349.0f, 589.0f, 349.0f, 572.0f)
                        quadTo(349.0f, 555.0f, 360.0f, 544.0f)
                        lineTo(704.0f, 200.0f)
                        lineTo(600.0f, 200.0f)
                        quadTo(583.0f, 200.0f, 571.5f, 188.5f)
                        quadTo(560.0f, 177.0f, 560.0f, 160.0f)
                        quadTo(560.0f, 143.0f, 571.5f, 131.5f)
                        quadTo(583.0f, 120.0f, 600.0f, 120.0f)
                        lineTo(800.0f, 120.0f)
                        quadTo(817.0f, 120.0f, 828.5f, 131.5f)
                        quadTo(840.0f, 143.0f, 840.0f, 160.0f)
                        lineTo(840.0f, 360.0f)
                        quadTo(840.0f, 377.0f, 828.5f, 388.5f)
                        quadTo(817.0f, 400.0f, 800.0f, 400.0f)
                        quadTo(783.0f, 400.0f, 771.5f, 388.5f)
                        quadTo(760.0f, 377.0f, 760.0f, 360.0f)
                        lineTo(760.0f, 256.0f)
                        close()
                    }
                }
                .build()
        return _openInNew!!
    }

private var _openInNew: ImageVector? = null
