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
val AnilibriaIcons.ChevronLeft: ImageVector
    get() {
        if (_chevronLeft != null) {
            return _chevronLeft!!
        }
        _chevronLeft =
            Builder(
                    name = "ChevronLeft",
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
                        moveTo(432.0f, 480.0f)
                        lineTo(588.0f, 636.0f)
                        quadTo(599.0f, 647.0f, 599.0f, 664.0f)
                        quadTo(599.0f, 681.0f, 588.0f, 692.0f)
                        quadTo(577.0f, 703.0f, 560.0f, 703.0f)
                        quadTo(543.0f, 703.0f, 532.0f, 692.0f)
                        lineTo(348.0f, 508.0f)
                        quadTo(342.0f, 502.0f, 339.5f, 495.0f)
                        quadTo(337.0f, 488.0f, 337.0f, 480.0f)
                        quadTo(337.0f, 472.0f, 339.5f, 465.0f)
                        quadTo(342.0f, 458.0f, 348.0f, 452.0f)
                        lineTo(532.0f, 268.0f)
                        quadTo(543.0f, 257.0f, 560.0f, 257.0f)
                        quadTo(577.0f, 257.0f, 588.0f, 268.0f)
                        quadTo(599.0f, 279.0f, 599.0f, 296.0f)
                        quadTo(599.0f, 313.0f, 588.0f, 324.0f)
                        lineTo(432.0f, 480.0f)
                        close()
                    }
                }
                .build()
        return _chevronLeft!!
    }

private var _chevronLeft: ImageVector? = null
