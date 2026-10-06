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
val AnilibertyIcons.Notifications: ImageVector
    get() {
        if (_notifications != null) {
            return _notifications!!
        }
        _notifications =
            Builder(
                    name = "Notifications",
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
                        moveTo(200.0f, 760.0f)
                        quadTo(183.0f, 760.0f, 171.5f, 748.5f)
                        quadTo(160.0f, 737.0f, 160.0f, 720.0f)
                        quadTo(160.0f, 703.0f, 171.5f, 691.5f)
                        quadTo(183.0f, 680.0f, 200.0f, 680.0f)
                        lineTo(240.0f, 680.0f)
                        lineTo(240.0f, 400.0f)
                        quadTo(240.0f, 317.0f, 290.0f, 252.5f)
                        quadTo(340.0f, 188.0f, 420.0f, 168.0f)
                        lineTo(420.0f, 140.0f)
                        quadTo(420.0f, 115.0f, 437.5f, 97.5f)
                        quadTo(455.0f, 80.0f, 480.0f, 80.0f)
                        quadTo(505.0f, 80.0f, 522.5f, 97.5f)
                        quadTo(540.0f, 115.0f, 540.0f, 140.0f)
                        lineTo(540.0f, 168.0f)
                        quadTo(620.0f, 188.0f, 670.0f, 252.5f)
                        quadTo(720.0f, 317.0f, 720.0f, 400.0f)
                        lineTo(720.0f, 680.0f)
                        lineTo(760.0f, 680.0f)
                        quadTo(777.0f, 680.0f, 788.5f, 691.5f)
                        quadTo(800.0f, 703.0f, 800.0f, 720.0f)
                        quadTo(800.0f, 737.0f, 788.5f, 748.5f)
                        quadTo(777.0f, 760.0f, 760.0f, 760.0f)
                        lineTo(200.0f, 760.0f)
                        close()
                        moveTo(480.0f, 460.0f)
                        close()
                        moveTo(480.0f, 880.0f)
                        quadTo(447.0f, 880.0f, 423.5f, 856.5f)
                        quadTo(400.0f, 833.0f, 400.0f, 800.0f)
                        lineTo(560.0f, 800.0f)
                        quadTo(560.0f, 833.0f, 536.5f, 856.5f)
                        quadTo(513.0f, 880.0f, 480.0f, 880.0f)
                        close()
                        moveTo(320.0f, 680.0f)
                        lineTo(640.0f, 680.0f)
                        lineTo(640.0f, 400.0f)
                        quadTo(640.0f, 334.0f, 593.0f, 287.0f)
                        quadTo(546.0f, 240.0f, 480.0f, 240.0f)
                        quadTo(414.0f, 240.0f, 367.0f, 287.0f)
                        quadTo(320.0f, 334.0f, 320.0f, 400.0f)
                        lineTo(320.0f, 680.0f)
                        close()
                    }
                }
                .build()
        return _notifications!!
    }

private var _notifications: ImageVector? = null
