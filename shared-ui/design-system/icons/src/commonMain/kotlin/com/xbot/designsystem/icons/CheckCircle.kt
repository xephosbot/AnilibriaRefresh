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
val AnilibertyIcons.Filled.CheckCircle: ImageVector
    get() {
        if (_checkCircleFilled != null) {
            return _checkCircleFilled!!
        }
        _checkCircleFilled =
            Builder(
                    name = "CheckCircleFilled",
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
                        moveTo(424.0f, 552.0f)
                        lineTo(338.0f, 466.0f)
                        quadTo(327.0f, 455.0f, 310.0f, 455.0f)
                        quadTo(293.0f, 455.0f, 282.0f, 466.0f)
                        quadTo(271.0f, 477.0f, 271.0f, 494.0f)
                        quadTo(271.0f, 511.0f, 282.0f, 522.0f)
                        lineTo(396.0f, 636.0f)
                        quadTo(408.0f, 648.0f, 424.0f, 648.0f)
                        quadTo(440.0f, 648.0f, 452.0f, 636.0f)
                        lineTo(678.0f, 410.0f)
                        quadTo(689.0f, 399.0f, 689.0f, 382.0f)
                        quadTo(689.0f, 365.0f, 678.0f, 354.0f)
                        quadTo(667.0f, 343.0f, 650.0f, 343.0f)
                        quadTo(633.0f, 343.0f, 622.0f, 354.0f)
                        lineTo(424.0f, 552.0f)
                        close()
                        moveTo(480.0f, 880.0f)
                        quadTo(397.0f, 880.0f, 324.0f, 848.5f)
                        quadTo(251.0f, 817.0f, 197.0f, 763.0f)
                        quadTo(143.0f, 709.0f, 111.5f, 636.0f)
                        quadTo(80.0f, 563.0f, 80.0f, 480.0f)
                        quadTo(80.0f, 397.0f, 111.5f, 324.0f)
                        quadTo(143.0f, 251.0f, 197.0f, 197.0f)
                        quadTo(251.0f, 143.0f, 324.0f, 111.5f)
                        quadTo(397.0f, 80.0f, 480.0f, 80.0f)
                        quadTo(563.0f, 80.0f, 636.0f, 111.5f)
                        quadTo(709.0f, 143.0f, 763.0f, 197.0f)
                        quadTo(817.0f, 251.0f, 848.5f, 324.0f)
                        quadTo(880.0f, 397.0f, 880.0f, 480.0f)
                        quadTo(880.0f, 563.0f, 848.5f, 636.0f)
                        quadTo(817.0f, 709.0f, 763.0f, 763.0f)
                        quadTo(709.0f, 817.0f, 636.0f, 848.5f)
                        quadTo(563.0f, 880.0f, 480.0f, 880.0f)
                        close()
                    }
                }
                .build()
        return _checkCircleFilled!!
    }

private var _checkCircleFilled: ImageVector? = null
