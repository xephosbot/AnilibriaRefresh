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
val AnilibertyIcons.Filled.NotificationsActive: ImageVector
    get() {
        if (_notificationsActiveFilled != null) {
            return _notificationsActiveFilled!!
        }
        _notificationsActiveFilled =
            Builder(
                name = "NotificationsActiveFilled",
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
                        moveTo(480.0f, 880.0f)
                        quadTo(447.0f, 880.0f, 423.5f, 856.5f)
                        quadTo(400.0f, 833.0f, 400.0f, 800.0f)
                        lineTo(560.0f, 800.0f)
                        quadTo(560.0f, 833.0f, 536.5f, 856.5f)
                        quadTo(513.0f, 880.0f, 480.0f, 880.0f)
                        close()
                        moveTo(120.0f, 400.0f)
                        quadTo(103.0f, 400.0f, 91.5f, 387.0f)
                        quadTo(80.0f, 374.0f, 82.0f, 357.0f)
                        quadTo(90.0f, 282.0f, 124.0f, 217.5f)
                        quadTo(158.0f, 153.0f, 211.0f, 105.0f)
                        quadTo(224.0f, 94.0f, 240.5f, 95.0f)
                        quadTo(257.0f, 96.0f, 267.0f, 110.0f)
                        quadTo(277.0f, 124.0f, 275.0f, 140.0f)
                        quadTo(273.0f, 156.0f, 260.0f, 168.0f)
                        quadTo(221.0f, 205.0f, 196.0f, 254.0f)
                        quadTo(171.0f, 303.0f, 163.0f, 360.0f)
                        quadTo(161.0f, 377.0f, 149.0f, 388.5f)
                        quadTo(137.0f, 400.0f, 120.0f, 400.0f)
                        close()
                        moveTo(840.0f, 400.0f)
                        quadTo(823.0f, 400.0f, 811.0f, 388.5f)
                        quadTo(799.0f, 377.0f, 797.0f, 360.0f)
                        quadTo(789.0f, 303.0f, 764.0f, 254.0f)
                        quadTo(739.0f, 205.0f, 700.0f, 168.0f)
                        quadTo(687.0f, 156.0f, 685.0f, 140.0f)
                        quadTo(683.0f, 124.0f, 693.0f, 110.0f)
                        quadTo(703.0f, 96.0f, 719.5f, 95.0f)
                        quadTo(736.0f, 94.0f, 749.0f, 105.0f)
                        quadTo(802.0f, 153.0f, 836.0f, 217.5f)
                        quadTo(870.0f, 282.0f, 878.0f, 357.0f)
                        quadTo(880.0f, 374.0f, 868.5f, 387.0f)
                        quadTo(857.0f, 400.0f, 840.0f, 400.0f)
                        close()
                    }
                }
                .build()
        return _notificationsActiveFilled!!
    }

private var _notificationsActiveFilled: ImageVector? = null
