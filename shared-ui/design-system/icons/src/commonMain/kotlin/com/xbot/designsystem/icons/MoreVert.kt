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
val AnilibriaIcons.MoreVert: ImageVector
    get() {
        if (_moreVert != null) {
            return _moreVert!!
        }
        _moreVert =
            Builder(
                    name = "MoreVert",
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
                        moveTo(480.0f, 800.0f)
                        quadTo(447.0f, 800.0f, 423.5f, 776.5f)
                        quadTo(400.0f, 753.0f, 400.0f, 720.0f)
                        quadTo(400.0f, 687.0f, 423.5f, 663.5f)
                        quadTo(447.0f, 640.0f, 480.0f, 640.0f)
                        quadTo(513.0f, 640.0f, 536.5f, 663.5f)
                        quadTo(560.0f, 687.0f, 560.0f, 720.0f)
                        quadTo(560.0f, 753.0f, 536.5f, 776.5f)
                        quadTo(513.0f, 800.0f, 480.0f, 800.0f)
                        close()
                        moveTo(480.0f, 560.0f)
                        quadTo(447.0f, 560.0f, 423.5f, 536.5f)
                        quadTo(400.0f, 513.0f, 400.0f, 480.0f)
                        quadTo(400.0f, 447.0f, 423.5f, 423.5f)
                        quadTo(447.0f, 400.0f, 480.0f, 400.0f)
                        quadTo(513.0f, 400.0f, 536.5f, 423.5f)
                        quadTo(560.0f, 447.0f, 560.0f, 480.0f)
                        quadTo(560.0f, 513.0f, 536.5f, 536.5f)
                        quadTo(513.0f, 560.0f, 480.0f, 560.0f)
                        close()
                        moveTo(480.0f, 320.0f)
                        quadTo(447.0f, 320.0f, 423.5f, 296.5f)
                        quadTo(400.0f, 273.0f, 400.0f, 240.0f)
                        quadTo(400.0f, 207.0f, 423.5f, 183.5f)
                        quadTo(447.0f, 160.0f, 480.0f, 160.0f)
                        quadTo(513.0f, 160.0f, 536.5f, 183.5f)
                        quadTo(560.0f, 207.0f, 560.0f, 240.0f)
                        quadTo(560.0f, 273.0f, 536.5f, 296.5f)
                        quadTo(513.0f, 320.0f, 480.0f, 320.0f)
                        close()
                    }
                }
                .build()
        return _moreVert!!
    }

private var _moreVert: ImageVector? = null
