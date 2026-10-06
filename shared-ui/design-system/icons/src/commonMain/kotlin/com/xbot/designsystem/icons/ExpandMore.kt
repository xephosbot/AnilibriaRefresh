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
val AnilibertyIcons.ExpandMore: ImageVector
    get() {
        if (_expandMore != null) {
            return _expandMore!!
        }
        _expandMore =
            Builder(
                name = "ExpandMore",
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
                        moveTo(480.0f, 598.0f)
                        quadTo(472.0f, 598.0f, 465.0f, 595.5f)
                        quadTo(458.0f, 593.0f, 452.0f, 587.0f)
                        lineTo(268.0f, 403.0f)
                        quadTo(257.0f, 392.0f, 257.0f, 375.0f)
                        quadTo(257.0f, 358.0f, 268.0f, 347.0f)
                        quadTo(279.0f, 336.0f, 296.0f, 336.0f)
                        quadTo(313.0f, 336.0f, 324.0f, 347.0f)
                        lineTo(480.0f, 503.0f)
                        lineTo(636.0f, 347.0f)
                        quadTo(647.0f, 336.0f, 664.0f, 336.0f)
                        quadTo(681.0f, 336.0f, 692.0f, 347.0f)
                        quadTo(703.0f, 358.0f, 703.0f, 375.0f)
                        quadTo(703.0f, 392.0f, 692.0f, 403.0f)
                        lineTo(508.0f, 587.0f)
                        quadTo(502.0f, 593.0f, 495.0f, 595.5f)
                        quadTo(488.0f, 598.0f, 480.0f, 598.0f)
                        close()
                    }
                }
                .build()
        return _expandMore!!
    }

private var _expandMore: ImageVector? = null
