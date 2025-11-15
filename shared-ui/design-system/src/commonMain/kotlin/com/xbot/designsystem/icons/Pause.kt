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
val AnilibriaIcons.Filled.Pause: ImageVector
    get() {
        if (_pauseFilled != null) {
            return _pauseFilled!!
        }
        _pauseFilled =
            Builder(
                    name = "PauseFilled",
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
                        moveTo(640.0f, 760.0f)
                        quadTo(607.0f, 760.0f, 583.5f, 736.5f)
                        quadTo(560.0f, 713.0f, 560.0f, 680.0f)
                        lineTo(560.0f, 280.0f)
                        quadTo(560.0f, 247.0f, 583.5f, 223.5f)
                        quadTo(607.0f, 200.0f, 640.0f, 200.0f)
                        lineTo(640.0f, 200.0f)
                        quadTo(673.0f, 200.0f, 696.5f, 223.5f)
                        quadTo(720.0f, 247.0f, 720.0f, 280.0f)
                        lineTo(720.0f, 680.0f)
                        quadTo(720.0f, 713.0f, 696.5f, 736.5f)
                        quadTo(673.0f, 760.0f, 640.0f, 760.0f)
                        close()
                        moveTo(320.0f, 760.0f)
                        quadTo(287.0f, 760.0f, 263.5f, 736.5f)
                        quadTo(240.0f, 713.0f, 240.0f, 680.0f)
                        lineTo(240.0f, 280.0f)
                        quadTo(240.0f, 247.0f, 263.5f, 223.5f)
                        quadTo(287.0f, 200.0f, 320.0f, 200.0f)
                        lineTo(320.0f, 200.0f)
                        quadTo(353.0f, 200.0f, 376.5f, 223.5f)
                        quadTo(400.0f, 247.0f, 400.0f, 280.0f)
                        lineTo(400.0f, 680.0f)
                        quadTo(400.0f, 713.0f, 376.5f, 736.5f)
                        quadTo(353.0f, 760.0f, 320.0f, 760.0f)
                        close()
                    }
                }
                .build()
        return _pauseFilled!!
    }

@Suppress("UnusedReceiverParameter")
val AnilibriaIcons.Outlined.Pause: ImageVector
    get() {
        if (_pauseOutlined != null) {
            return _pauseOutlined!!
        }
        _pauseOutlined =
            Builder(
                name = "PauseOutlined",
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
                        moveTo(600.0f, 760.0f)
                        quadTo(567.0f, 760.0f, 543.5f, 736.5f)
                        quadTo(520.0f, 713.0f, 520.0f, 680.0f)
                        lineTo(520.0f, 280.0f)
                        quadTo(520.0f, 247.0f, 543.5f, 223.5f)
                        quadTo(567.0f, 200.0f, 600.0f, 200.0f)
                        lineTo(680.0f, 200.0f)
                        quadTo(713.0f, 200.0f, 736.5f, 223.5f)
                        quadTo(760.0f, 247.0f, 760.0f, 280.0f)
                        lineTo(760.0f, 680.0f)
                        quadTo(760.0f, 713.0f, 736.5f, 736.5f)
                        quadTo(713.0f, 760.0f, 680.0f, 760.0f)
                        lineTo(600.0f, 760.0f)
                        close()
                        moveTo(280.0f, 760.0f)
                        quadTo(247.0f, 760.0f, 223.5f, 736.5f)
                        quadTo(200.0f, 713.0f, 200.0f, 680.0f)
                        lineTo(200.0f, 280.0f)
                        quadTo(200.0f, 247.0f, 223.5f, 223.5f)
                        quadTo(247.0f, 200.0f, 280.0f, 200.0f)
                        lineTo(360.0f, 200.0f)
                        quadTo(393.0f, 200.0f, 416.5f, 223.5f)
                        quadTo(440.0f, 247.0f, 440.0f, 280.0f)
                        lineTo(440.0f, 680.0f)
                        quadTo(440.0f, 713.0f, 416.5f, 736.5f)
                        quadTo(393.0f, 760.0f, 360.0f, 760.0f)
                        lineTo(280.0f, 760.0f)
                        close()
                        moveTo(600.0f, 680.0f)
                        lineTo(680.0f, 680.0f)
                        lineTo(680.0f, 280.0f)
                        lineTo(600.0f, 280.0f)
                        lineTo(600.0f, 680.0f)
                        close()
                        moveTo(280.0f, 680.0f)
                        lineTo(360.0f, 680.0f)
                        lineTo(360.0f, 280.0f)
                        lineTo(280.0f, 280.0f)
                        lineTo(280.0f, 680.0f)
                        close()
                        moveTo(280.0f, 280.0f)
                        lineTo(280.0f, 280.0f)
                        lineTo(280.0f, 680.0f)
                        lineTo(280.0f, 680.0f)
                        lineTo(280.0f, 280.0f)
                        close()
                        moveTo(600.0f, 280.0f)
                        lineTo(600.0f, 280.0f)
                        lineTo(600.0f, 680.0f)
                        lineTo(600.0f, 680.0f)
                        lineTo(600.0f, 280.0f)
                        close()
                    }
                }
                .build()
        return _pauseOutlined!!
    }


private var _pauseFilled: ImageVector? = null
private var _pauseOutlined: ImageVector? = null
