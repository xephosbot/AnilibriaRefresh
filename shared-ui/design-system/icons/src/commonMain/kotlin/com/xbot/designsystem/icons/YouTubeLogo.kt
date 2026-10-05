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
val AnilibertyIcons.YouTubeLogo: ImageVector
    get() {
        if (_youTubeLogo != null) {
            return _youTubeLogo!!
        }
        _youTubeLogo =
            Builder(
                name = "YouTube Logo",
                defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp,
                viewportWidth = 24.0f,
                viewportHeight = 24.0f
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
                        moveTo(23.498f, 6.186f)
                        arcTo(3.016f, 3.016f, 0.0f, false, false, 21.376f, 4.05f)
                        curveTo(19.505f, 3.545f, 12.0f, 3.545f, 12.0f, 3.545f)
                        curveTo(12.0f, 3.545f, 4.495f, 3.545f, 2.623f, 4.05f)
                        arcTo(3.017f, 3.017f, 0.0f, false, false, 0.502f, 6.186f)
                        curveTo(0.0f, 8.07f, 0.0f, 12.0f, 0.0f, 12.0f)
                        curveTo(0.0f, 12.0f, 0.0f, 15.93f, 0.502f, 17.814f)
                        arcTo(3.016f, 3.016f, 0.0f, false, false, 2.624f, 19.95f)
                        curveTo(4.495f, 20.455f, 12.0f, 20.455f, 12.0f, 20.455f)
                        curveTo(12.0f, 20.455f, 19.505f, 20.455f, 21.377f, 19.95f)
                        arcTo(3.015f, 3.015f, 0.0f, false, false, 23.499f, 17.814f)
                        curveTo(24.0f, 15.93f, 24.0f, 12.0f, 24.0f, 12.0f)
                        curveTo(24.0f, 12.0f, 24.0f, 8.07f, 23.498f, 6.186f)
                        close()
                        moveTo(9.545f, 15.568f)
                        lineTo(9.545f, 8.432f)
                        lineTo(15.818f, 12.0f)
                        lineTo(9.545f, 15.568f)
                        close()
                    }
                }
                .build()
        return _youTubeLogo!!
    }

private var _youTubeLogo: ImageVector? = null
