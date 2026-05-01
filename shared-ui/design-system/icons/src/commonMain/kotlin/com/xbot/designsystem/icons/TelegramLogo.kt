package com.xbot.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("UnusedReceiverParameter")
val AnilibriaIcons.TelegramLogo: ImageVector
    get() {
        if (_telegramLogo != null) {
            return _telegramLogo!!
        }
        _telegramLogo = Builder(
            name = "Telegram Logo",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFffffff)),
                stroke = null,
                strokeLineWidth = 0.0f,
                strokeLineCap = Butt,
                strokeLineJoin = Miter,
                strokeLineMiter = 4.0f,
                pathFillType = EvenOdd
            ) {
                moveTo(3.369f, 11.361f)
                curveTo(8.737f, 9.03f, 12.31f, 7.481f, 14.105f, 6.73f)
                curveTo(19.212f, 4.598f, 20.285f, 4.23f, 20.975f, 4.215f)
                curveTo(21.128f, 4.215f, 21.466f, 4.246f, 21.696f, 4.43f)
                curveTo(21.88f, 4.583f, 21.926f, 4.782f, 21.957f, 4.936f)
                curveTo(21.987f, 5.089f, 22.018f, 5.411f, 21.987f, 5.656f)
                curveTo(21.711f, 8.57f, 20.515f, 15.64f, 19.902f, 18.892f)
                curveTo(19.641f, 20.272f, 19.135f, 20.732f, 18.644f, 20.778f)
                curveTo(17.57f, 20.87f, 16.758f, 20.072f, 15.73f, 19.398f)
                curveTo(14.105f, 18.339f, 13.2f, 17.68f, 11.62f, 16.637f)
                curveTo(9.795f, 15.441f, 10.976f, 14.781f, 12.019f, 13.708f)
                curveTo(12.295f, 13.432f, 17.003f, 9.138f, 17.095f, 8.754f)
                curveTo(17.11f, 8.708f, 17.11f, 8.524f, 17.003f, 8.432f)
                curveTo(16.896f, 8.34f, 16.742f, 8.371f, 16.62f, 8.402f)
                curveTo(16.451f, 8.432f, 13.875f, 10.15f, 8.86f, 13.539f)
                curveTo(8.124f, 14.045f, 7.464f, 14.291f, 6.866f, 14.275f)
                curveTo(6.207f, 14.26f, 4.949f, 13.907f, 3.998f, 13.601f)
                curveTo(2.848f, 13.233f, 1.928f, 13.033f, 2.004f, 12.389f)
                curveTo(2.05f, 12.052f, 2.511f, 11.714f, 3.369f, 11.361f)
                close()
            }
        }
        .build()
        return _telegramLogo!!
    }

private var _telegramLogo: ImageVector? = null
