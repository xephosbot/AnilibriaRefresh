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
val AnilibriaIcons.Checklist: ImageVector
    get() {
        if (_checklist != null) {
            return _checklist!!
        }
        _checklist =
            Builder(
                name = "Checklist",
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
                        moveTo(221.0f, 647.0f)
                        lineTo(363.0f, 505.0f)
                        quadTo(375.0f, 493.0f, 391.0f, 493.5f)
                        quadTo(407.0f, 494.0f, 419.0f, 506.0f)
                        quadTo(430.0f, 518.0f, 430.0f, 534.0f)
                        quadTo(430.0f, 550.0f, 419.0f, 562.0f)
                        lineTo(250.0f, 732.0f)
                        quadTo(238.0f, 744.0f, 222.0f, 744.0f)
                        quadTo(206.0f, 744.0f, 194.0f, 732.0f)
                        lineTo(108.0f, 646.0f)
                        quadTo(97.0f, 635.0f, 97.0f, 618.0f)
                        quadTo(97.0f, 601.0f, 108.0f, 590.0f)
                        quadTo(119.0f, 579.0f, 136.0f, 579.0f)
                        quadTo(153.0f, 579.0f, 164.0f, 590.0f)
                        lineTo(221.0f, 647.0f)
                        close()
                        moveTo(221.0f, 327.0f)
                        lineTo(363.0f, 185.0f)
                        quadTo(375.0f, 173.0f, 391.0f, 173.5f)
                        quadTo(407.0f, 174.0f, 419.0f, 186.0f)
                        quadTo(430.0f, 198.0f, 430.0f, 214.0f)
                        quadTo(430.0f, 230.0f, 419.0f, 242.0f)
                        lineTo(250.0f, 412.0f)
                        quadTo(238.0f, 424.0f, 222.0f, 424.0f)
                        quadTo(206.0f, 424.0f, 194.0f, 412.0f)
                        lineTo(108.0f, 326.0f)
                        quadTo(97.0f, 315.0f, 97.0f, 298.0f)
                        quadTo(97.0f, 281.0f, 108.0f, 270.0f)
                        quadTo(119.0f, 259.0f, 136.0f, 259.0f)
                        quadTo(153.0f, 259.0f, 164.0f, 270.0f)
                        lineTo(221.0f, 327.0f)
                        close()
                        moveTo(560.0f, 680.0f)
                        quadTo(543.0f, 680.0f, 531.5f, 668.5f)
                        quadTo(520.0f, 657.0f, 520.0f, 640.0f)
                        quadTo(520.0f, 623.0f, 531.5f, 611.5f)
                        quadTo(543.0f, 600.0f, 560.0f, 600.0f)
                        lineTo(840.0f, 600.0f)
                        quadTo(857.0f, 600.0f, 868.5f, 611.5f)
                        quadTo(880.0f, 623.0f, 880.0f, 640.0f)
                        quadTo(880.0f, 657.0f, 868.5f, 668.5f)
                        quadTo(857.0f, 680.0f, 840.0f, 680.0f)
                        lineTo(560.0f, 680.0f)
                        close()
                        moveTo(560.0f, 360.0f)
                        quadTo(543.0f, 360.0f, 531.5f, 348.5f)
                        quadTo(520.0f, 337.0f, 520.0f, 320.0f)
                        quadTo(520.0f, 303.0f, 531.5f, 291.5f)
                        quadTo(543.0f, 280.0f, 560.0f, 280.0f)
                        lineTo(840.0f, 280.0f)
                        quadTo(857.0f, 280.0f, 868.5f, 291.5f)
                        quadTo(880.0f, 303.0f, 880.0f, 320.0f)
                        quadTo(880.0f, 337.0f, 868.5f, 348.5f)
                        quadTo(857.0f, 360.0f, 840.0f, 360.0f)
                        lineTo(560.0f, 360.0f)
                        close()
                    }
                }
                .build()
        return _checklist!!
    }

private var _checklist: ImageVector? = null
