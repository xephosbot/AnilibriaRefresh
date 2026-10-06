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
val AnilibertyIcons.Tv: ImageVector
    get() {
        if (_tv != null) {
            return _tv!!
        }
        _tv =
            Builder(
                    name = "Tv",
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
                        moveTo(160.0f, 760.0f)
                        quadTo(127.0f, 760.0f, 103.5f, 736.5f)
                        quadTo(80.0f, 713.0f, 80.0f, 680.0f)
                        lineTo(80.0f, 200.0f)
                        quadTo(80.0f, 167.0f, 103.5f, 143.5f)
                        quadTo(127.0f, 120.0f, 160.0f, 120.0f)
                        lineTo(800.0f, 120.0f)
                        quadTo(833.0f, 120.0f, 856.5f, 143.5f)
                        quadTo(880.0f, 167.0f, 880.0f, 200.0f)
                        lineTo(880.0f, 680.0f)
                        quadTo(880.0f, 713.0f, 856.5f, 736.5f)
                        quadTo(833.0f, 760.0f, 800.0f, 760.0f)
                        lineTo(640.0f, 760.0f)
                        lineTo(640.0f, 800.0f)
                        quadTo(640.0f, 817.0f, 628.5f, 828.5f)
                        quadTo(617.0f, 840.0f, 600.0f, 840.0f)
                        lineTo(360.0f, 840.0f)
                        quadTo(343.0f, 840.0f, 331.5f, 828.5f)
                        quadTo(320.0f, 817.0f, 320.0f, 800.0f)
                        lineTo(320.0f, 760.0f)
                        lineTo(160.0f, 760.0f)
                        close()
                        moveTo(160.0f, 680.0f)
                        lineTo(800.0f, 680.0f)
                        lineTo(800.0f, 200.0f)
                        lineTo(160.0f, 200.0f)
                        lineTo(160.0f, 680.0f)
                        close()
                        moveTo(160.0f, 680.0f)
                        lineTo(160.0f, 200.0f)
                        lineTo(160.0f, 680.0f)
                        close()
                    }
                }
                .build()
        return _tv!!
    }

private var _tv: ImageVector? = null
