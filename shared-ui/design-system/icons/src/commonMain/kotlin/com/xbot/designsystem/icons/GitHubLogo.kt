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
val AnilibertyIcons.GitHubLogo: ImageVector
    get() {
        if (_gitHubLogo != null) {
            return _gitHubLogo!!
        }
        _gitHubLogo =
            Builder(
                    name = "GitHub Logo",
                    defaultWidth = 24.0.dp,
                    defaultHeight = 24.0.dp,
                    viewportWidth = 24.0f,
                    viewportHeight = 24.0f,
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
                        moveTo(12.0f, 0.297f)
                        curveTo(5.37f, 0.297f, 0.0f, 5.67f, 0.0f, 12.297f)
                        curveTo(0.0f, 17.6f, 3.438f, 22.097f, 8.205f, 23.682f)
                        curveTo(8.805f, 23.795f, 9.025f, 23.424f, 9.025f, 23.105f)
                        curveTo(9.025f, 22.82f, 9.015f, 22.065f, 9.01f, 21.065f)
                        curveTo(5.672f, 21.789f, 4.968f, 19.455f, 4.968f, 19.455f)
                        curveTo(4.422f, 18.07f, 3.633f, 17.7f, 3.633f, 17.7f)
                        curveTo(2.546f, 16.956f, 3.717f, 16.971f, 3.717f, 16.971f)
                        curveTo(4.922f, 17.055f, 5.555f, 18.207f, 5.555f, 18.207f)
                        curveTo(6.625f, 20.042f, 8.364f, 19.512f, 9.05f, 19.205f)
                        curveTo(9.158f, 18.429f, 9.467f, 17.9f, 9.81f, 17.6f)
                        curveTo(7.145f, 17.3f, 4.344f, 16.268f, 4.344f, 11.67f)
                        curveTo(4.344f, 10.36f, 4.809f, 9.29f, 5.579f, 8.45f)
                        curveTo(5.444f, 8.147f, 5.039f, 6.927f, 5.684f, 5.274f)
                        curveTo(5.684f, 5.274f, 6.689f, 4.952f, 8.984f, 6.504f)
                        curveTo(9.944f, 6.237f, 10.964f, 6.105f, 11.984f, 6.099f)
                        curveTo(13.004f, 6.105f, 14.024f, 6.237f, 14.984f, 6.504f)
                        curveTo(17.264f, 4.952f, 18.269f, 5.274f, 18.269f, 5.274f)
                        curveTo(18.914f, 6.927f, 18.509f, 8.147f, 18.389f, 8.45f)
                        curveTo(19.154f, 9.29f, 19.619f, 10.36f, 19.619f, 11.67f)
                        curveTo(19.619f, 16.28f, 16.814f, 17.295f, 14.144f, 17.59f)
                        curveTo(14.564f, 17.95f, 14.954f, 18.686f, 14.954f, 19.81f)
                        curveTo(14.954f, 21.416f, 14.939f, 22.706f, 14.939f, 23.096f)
                        curveTo(14.939f, 23.411f, 15.149f, 23.786f, 15.764f, 23.666f)
                        curveTo(20.565f, 22.092f, 24.0f, 17.592f, 24.0f, 12.297f)
                        curveTo(24.0f, 5.67f, 18.627f, 0.297f, 12.0f, 0.297f)
                    }
                }
                .build()
        return _gitHubLogo!!
    }

private var _gitHubLogo: ImageVector? = null
