package com.xbot.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("UnusedReceiverParameter")
val AnilibriaIcons.Filled.AnilibriaLogo: ImageVector
    get() {
        if (_anilibriaLogo != null) {
            return _anilibriaLogo!!
        }
        _anilibriaLogo = ImageVector.Builder(
            name = "AnilibriaLogo",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFB4AF)),
                stroke = null,
                strokeLineWidth = 0.0f,
                strokeLineCap = Butt,
                strokeLineJoin = Miter,
                strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(12.0737f, 22.5355f)
                lineTo(12.3696f, 23.0775f)
                lineTo(24.0f, 17.3117f)
                lineTo(23.3592f, 15.7347f)
                lineTo(12.0737f, 22.5355f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFB4AF)),
                stroke = null,
                strokeLineWidth = 0.0f,
                strokeLineCap = Butt,
                strokeLineJoin = Miter,
                strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(7.0469f, 23.0776f)
                horizontalLineTo(2.9569f)
                lineTo(11.4825f, 1.0f)
                horizontalLineTo(14.1435f)
                lineTo(21.3384f, 23.0776f)
                horizontalLineTo(17.1987f)
                lineTo(12.0737f, 5.6818f)
                lineTo(7.0469f, 23.0776f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFB4AF)),
                stroke = null,
                strokeLineWidth = 0.0f,
                strokeLineCap = Butt,
                strokeLineJoin = Miter,
                strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(2.9569f, 1.0f)
                lineTo(0.0f, 2.7249f)
                lineTo(11.1867f, 21.0573f)
                lineTo(22.7675f, 14.1086f)
                lineTo(21.4864f, 10.708f)
                lineTo(12.4678f, 16.0306f)
                lineTo(2.9569f, 1.0f)
                close()
            }
        }
            .build()
        return _anilibriaLogo!!
    }

@Suppress("ObjectPropertyName")
private var _anilibriaLogo: ImageVector? = null
