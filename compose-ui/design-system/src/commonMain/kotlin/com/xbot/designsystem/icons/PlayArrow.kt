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
val AnilibriaIcons.Filled.PlayArrow: ImageVector
    get() {
        if (_playArrowFilled != null) {
            return _playArrowFilled!!
        }
        _playArrowFilled =
            Builder(
                    name = "PlayArrowFilled",
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
                        moveTo(320.0f, 687.0f)
                        lineTo(320.0f, 273.0f)
                        quadTo(320.0f, 256.0f, 332.0f, 244.5f)
                        quadTo(344.0f, 233.0f, 360.0f, 233.0f)
                        quadTo(365.0f, 233.0f, 370.5f, 234.5f)
                        quadTo(376.0f, 236.0f, 381.0f, 239.0f)
                        lineTo(707.0f, 446.0f)
                        quadTo(716.0f, 452.0f, 720.5f, 461.0f)
                        quadTo(725.0f, 470.0f, 725.0f, 480.0f)
                        quadTo(725.0f, 490.0f, 720.5f, 499.0f)
                        quadTo(716.0f, 508.0f, 707.0f, 514.0f)
                        lineTo(381.0f, 721.0f)
                        quadTo(376.0f, 724.0f, 370.5f, 725.5f)
                        quadTo(365.0f, 727.0f, 360.0f, 727.0f)
                        quadTo(344.0f, 727.0f, 332.0f, 715.5f)
                        quadTo(320.0f, 704.0f, 320.0f, 687.0f)
                        close()
                    }
                }
                .build()
        return _playArrowFilled!!
    }

@Suppress("UnusedReceiverParameter")
val AnilibriaIcons.Outlined.PlayArrow: ImageVector
    get() {
        if (_playArrowOutlined != null) {
            return _playArrowOutlined!!
        }
        _playArrowOutlined =
            Builder(
                name = "PlayArrowOutlined",
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
                        moveTo(320.0f, 687.0f)
                        lineTo(320.0f, 273.0f)
                        quadTo(320.0f, 256.0f, 332.0f, 244.5f)
                        quadTo(344.0f, 233.0f, 360.0f, 233.0f)
                        quadTo(365.0f, 233.0f, 370.5f, 234.5f)
                        quadTo(376.0f, 236.0f, 381.0f, 239.0f)
                        lineTo(707.0f, 446.0f)
                        quadTo(716.0f, 452.0f, 720.5f, 461.0f)
                        quadTo(725.0f, 470.0f, 725.0f, 480.0f)
                        quadTo(725.0f, 490.0f, 720.5f, 499.0f)
                        quadTo(716.0f, 508.0f, 707.0f, 514.0f)
                        lineTo(381.0f, 721.0f)
                        quadTo(376.0f, 724.0f, 370.5f, 725.5f)
                        quadTo(365.0f, 727.0f, 360.0f, 727.0f)
                        quadTo(344.0f, 727.0f, 332.0f, 715.5f)
                        quadTo(320.0f, 704.0f, 320.0f, 687.0f)
                        close()
                        moveTo(400.0f, 480.0f)
                        lineTo(400.0f, 480.0f)
                        lineTo(400.0f, 480.0f)
                        close()
                        moveTo(400.0f, 614.0f)
                        lineTo(610.0f, 480.0f)
                        lineTo(400.0f, 346.0f)
                        lineTo(400.0f, 614.0f)
                        close()
                    }
                }
                .build()
        return _playArrowOutlined!!
    }


private var _playArrowFilled: ImageVector? = null
private var _playArrowOutlined: ImageVector? = null
