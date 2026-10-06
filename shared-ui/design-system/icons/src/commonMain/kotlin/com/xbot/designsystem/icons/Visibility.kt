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
val AnilibertyIcons.Visibility: ImageVector
    get() {
        if (_visibility != null) {
            return _visibility!!
        }
        _visibility =
            Builder(
                name = "Visibility",
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
                        moveTo(480.0f, 640.0f)
                        quadTo(555.0f, 640.0f, 607.5f, 587.5f)
                        quadTo(660.0f, 535.0f, 660.0f, 460.0f)
                        quadTo(660.0f, 385.0f, 607.5f, 332.5f)
                        quadTo(555.0f, 280.0f, 480.0f, 280.0f)
                        quadTo(405.0f, 280.0f, 352.5f, 332.5f)
                        quadTo(300.0f, 385.0f, 300.0f, 460.0f)
                        quadTo(300.0f, 535.0f, 352.5f, 587.5f)
                        quadTo(405.0f, 640.0f, 480.0f, 640.0f)
                        close()
                        moveTo(480.0f, 568.0f)
                        quadTo(435.0f, 568.0f, 403.5f, 536.5f)
                        quadTo(372.0f, 505.0f, 372.0f, 460.0f)
                        quadTo(372.0f, 415.0f, 403.5f, 383.5f)
                        quadTo(435.0f, 352.0f, 480.0f, 352.0f)
                        quadTo(525.0f, 352.0f, 556.5f, 383.5f)
                        quadTo(588.0f, 415.0f, 588.0f, 460.0f)
                        quadTo(588.0f, 505.0f, 556.5f, 536.5f)
                        quadTo(525.0f, 568.0f, 480.0f, 568.0f)
                        close()
                        moveTo(480.0f, 760.0f)
                        quadTo(346.0f, 760.0f, 235.5f, 688.0f)
                        quadTo(125.0f, 616.0f, 61.0f, 498.0f)
                        quadTo(56.0f, 489.0f, 53.5f, 479.5f)
                        quadTo(51.0f, 470.0f, 51.0f, 460.0f)
                        quadTo(51.0f, 450.0f, 53.5f, 440.5f)
                        quadTo(56.0f, 431.0f, 61.0f, 422.0f)
                        quadTo(125.0f, 304.0f, 235.5f, 232.0f)
                        quadTo(346.0f, 160.0f, 480.0f, 160.0f)
                        quadTo(614.0f, 160.0f, 724.5f, 232.0f)
                        quadTo(835.0f, 304.0f, 899.0f, 422.0f)
                        quadTo(904.0f, 431.0f, 906.5f, 440.5f)
                        quadTo(909.0f, 450.0f, 909.0f, 460.0f)
                        quadTo(909.0f, 470.0f, 906.5f, 479.5f)
                        quadTo(904.0f, 489.0f, 899.0f, 498.0f)
                        quadTo(835.0f, 616.0f, 724.5f, 688.0f)
                        quadTo(614.0f, 760.0f, 480.0f, 760.0f)
                        close()
                        moveTo(480.0f, 460.0f)
                        close()
                        moveTo(480.0f, 680.0f)
                        quadTo(593.0f, 680.0f, 687.5f, 620.5f)
                        quadTo(782.0f, 561.0f, 832.0f, 460.0f)
                        quadTo(782.0f, 359.0f, 687.5f, 299.5f)
                        quadTo(593.0f, 240.0f, 480.0f, 240.0f)
                        quadTo(367.0f, 240.0f, 272.5f, 299.5f)
                        quadTo(178.0f, 359.0f, 128.0f, 460.0f)
                        quadTo(178.0f, 561.0f, 272.5f, 620.5f)
                        quadTo(367.0f, 680.0f, 480.0f, 680.0f)
                        close()
                    }
                }
                .build()
        return _visibility!!
    }

private var _visibility: ImageVector? = null
