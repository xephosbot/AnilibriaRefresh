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
val AnilibertyIcons.SwapVert: ImageVector
    get() {
        if (_swapVert != null) {
            return _swapVert!!
        }
        _swapVert =
            Builder(
                    name = "SwapVert",
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
                        moveTo(360.0f, 520.0f)
                        quadTo(343.0f, 520.0f, 331.5f, 508.5f)
                        quadTo(320.0f, 497.0f, 320.0f, 480.0f)
                        lineTo(320.0f, 233.0f)
                        lineTo(245.0f, 308.0f)
                        quadTo(234.0f, 319.0f, 217.5f, 319.0f)
                        quadTo(201.0f, 319.0f, 189.0f, 308.0f)
                        quadTo(177.0f, 296.0f, 177.0f, 279.5f)
                        quadTo(177.0f, 263.0f, 189.0f, 251.0f)
                        lineTo(332.0f, 108.0f)
                        quadTo(338.0f, 102.0f, 345.0f, 99.5f)
                        quadTo(352.0f, 97.0f, 360.0f, 97.0f)
                        quadTo(368.0f, 97.0f, 375.0f, 99.5f)
                        quadTo(382.0f, 102.0f, 388.0f, 108.0f)
                        lineTo(532.0f, 252.0f)
                        quadTo(544.0f, 264.0f, 543.5f, 280.0f)
                        quadTo(543.0f, 296.0f, 531.0f, 308.0f)
                        quadTo(519.0f, 319.0f, 503.0f, 319.5f)
                        quadTo(487.0f, 320.0f, 475.0f, 308.0f)
                        lineTo(400.0f, 233.0f)
                        lineTo(400.0f, 480.0f)
                        quadTo(400.0f, 497.0f, 388.5f, 508.5f)
                        quadTo(377.0f, 520.0f, 360.0f, 520.0f)
                        close()
                        moveTo(600.0f, 863.0f)
                        quadTo(592.0f, 863.0f, 585.0f, 860.5f)
                        quadTo(578.0f, 858.0f, 572.0f, 852.0f)
                        lineTo(428.0f, 708.0f)
                        quadTo(416.0f, 696.0f, 416.5f, 680.0f)
                        quadTo(417.0f, 664.0f, 429.0f, 652.0f)
                        quadTo(441.0f, 641.0f, 457.0f, 640.5f)
                        quadTo(473.0f, 640.0f, 485.0f, 652.0f)
                        lineTo(560.0f, 727.0f)
                        lineTo(560.0f, 480.0f)
                        quadTo(560.0f, 463.0f, 571.5f, 451.5f)
                        quadTo(583.0f, 440.0f, 600.0f, 440.0f)
                        quadTo(617.0f, 440.0f, 628.5f, 451.5f)
                        quadTo(640.0f, 463.0f, 640.0f, 480.0f)
                        lineTo(640.0f, 727.0f)
                        lineTo(715.0f, 652.0f)
                        quadTo(726.0f, 641.0f, 742.5f, 641.0f)
                        quadTo(759.0f, 641.0f, 771.0f, 652.0f)
                        quadTo(783.0f, 664.0f, 783.0f, 680.5f)
                        quadTo(783.0f, 697.0f, 771.0f, 709.0f)
                        lineTo(628.0f, 852.0f)
                        quadTo(622.0f, 858.0f, 615.0f, 860.5f)
                        quadTo(608.0f, 863.0f, 600.0f, 863.0f)
                        close()
                    }
                }
                .build()
        return _swapVert!!
    }

private var _swapVert: ImageVector? = null
