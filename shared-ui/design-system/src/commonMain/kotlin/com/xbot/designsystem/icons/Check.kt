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
val AnilibriaIcons.Check: ImageVector
    get() {
        if (_check != null) {
            return _check!!
        }
        _check =
            Builder(
                name = "Check",
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
                        moveTo(382.0f, 606.0f)
                        lineTo(721.0f, 267.0f)
                        quadTo(733.0f, 255.0f, 749.0f, 255.0f)
                        quadTo(765.0f, 255.0f, 777.0f, 267.0f)
                        quadTo(789.0f, 279.0f, 789.0f, 295.5f)
                        quadTo(789.0f, 312.0f, 777.0f, 324.0f)
                        lineTo(410.0f, 692.0f)
                        quadTo(398.0f, 704.0f, 382.0f, 704.0f)
                        quadTo(366.0f, 704.0f, 354.0f, 692.0f)
                        lineTo(182.0f, 520.0f)
                        quadTo(170.0f, 508.0f, 170.5f, 491.5f)
                        quadTo(171.0f, 475.0f, 183.0f, 463.0f)
                        quadTo(195.0f, 451.0f, 211.5f, 451.0f)
                        quadTo(228.0f, 451.0f, 240.0f, 463.0f)
                        lineTo(382.0f, 606.0f)
                        close()
                    }
                }
                .build()
        return _check!!
    }

private var _check: ImageVector? = null
