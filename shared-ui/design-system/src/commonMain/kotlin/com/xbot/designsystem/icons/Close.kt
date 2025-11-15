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
val AnilibriaIcons.Close: ImageVector
    get() {
        if (_close != null) {
            return _close!!
        }
        _close =
            Builder(
                    name = "Close",
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
                        moveTo(480.0f, 536.0f)
                        lineTo(284.0f, 732.0f)
                        quadTo(273.0f, 743.0f, 256.0f, 743.0f)
                        quadTo(239.0f, 743.0f, 228.0f, 732.0f)
                        quadTo(217.0f, 721.0f, 217.0f, 704.0f)
                        quadTo(217.0f, 687.0f, 228.0f, 676.0f)
                        lineTo(424.0f, 480.0f)
                        lineTo(228.0f, 284.0f)
                        quadTo(217.0f, 273.0f, 217.0f, 256.0f)
                        quadTo(217.0f, 239.0f, 228.0f, 228.0f)
                        quadTo(239.0f, 217.0f, 256.0f, 217.0f)
                        quadTo(273.0f, 217.0f, 284.0f, 228.0f)
                        lineTo(480.0f, 424.0f)
                        lineTo(676.0f, 228.0f)
                        quadTo(687.0f, 217.0f, 704.0f, 217.0f)
                        quadTo(721.0f, 217.0f, 732.0f, 228.0f)
                        quadTo(743.0f, 239.0f, 743.0f, 256.0f)
                        quadTo(743.0f, 273.0f, 732.0f, 284.0f)
                        lineTo(536.0f, 480.0f)
                        lineTo(732.0f, 676.0f)
                        quadTo(743.0f, 687.0f, 743.0f, 704.0f)
                        quadTo(743.0f, 721.0f, 732.0f, 732.0f)
                        quadTo(721.0f, 743.0f, 704.0f, 743.0f)
                        quadTo(687.0f, 743.0f, 676.0f, 732.0f)
                        lineTo(480.0f, 536.0f)
                        close()
                    }
                }
                .build()
        return _close!!
    }

private var _close: ImageVector? = null
