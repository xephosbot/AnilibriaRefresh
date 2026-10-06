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
val AnilibertyIcons.Bookmark: ImageVector
    get() {
        if (_bookmark != null) {
            return _bookmark!!
        }
        _bookmark =
            Builder(
                name = "Bookmark",
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
                        moveTo(480.0f, 720.0f)
                        lineTo(312.0f, 792.0f)
                        quadTo(272.0f, 809.0f, 236.0f, 785.5f)
                        quadTo(200.0f, 762.0f, 200.0f, 719.0f)
                        lineTo(200.0f, 200.0f)
                        quadTo(200.0f, 167.0f, 223.5f, 143.5f)
                        quadTo(247.0f, 120.0f, 280.0f, 120.0f)
                        lineTo(680.0f, 120.0f)
                        quadTo(713.0f, 120.0f, 736.5f, 143.5f)
                        quadTo(760.0f, 167.0f, 760.0f, 200.0f)
                        lineTo(760.0f, 719.0f)
                        quadTo(760.0f, 762.0f, 724.0f, 785.5f)
                        quadTo(688.0f, 809.0f, 648.0f, 792.0f)
                        lineTo(480.0f, 720.0f)
                        close()
                        moveTo(480.0f, 632.0f)
                        lineTo(680.0f, 718.0f)
                        lineTo(680.0f, 200.0f)
                        lineTo(280.0f, 200.0f)
                        lineTo(280.0f, 718.0f)
                        lineTo(480.0f, 632.0f)
                        close()
                        moveTo(480.0f, 200.0f)
                        lineTo(280.0f, 200.0f)
                        lineTo(680.0f, 200.0f)
                        lineTo(480.0f, 200.0f)
                        close()
                    }
                }
                .build()
        return _bookmark!!
    }

private var _bookmark: ImageVector? = null
