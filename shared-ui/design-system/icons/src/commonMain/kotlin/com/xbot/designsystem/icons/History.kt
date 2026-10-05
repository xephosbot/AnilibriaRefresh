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
val AnilibertyIcons.History: ImageVector
    get() {
        if (_history != null) {
            return _history!!
        }
        _history =
            Builder(
                name = "History",
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
                        moveTo(480.0f, 840.0f)
                        quadTo(354.0f, 840.0f, 257.0f, 763.5f)
                        quadTo(160.0f, 687.0f, 131.0f, 568.0f)
                        quadTo(127.0f, 553.0f, 137.0f, 540.5f)
                        quadTo(147.0f, 528.0f, 164.0f, 526.0f)
                        quadTo(180.0f, 524.0f, 193.0f, 532.0f)
                        quadTo(206.0f, 540.0f, 211.0f, 556.0f)
                        quadTo(235.0f, 646.0f, 310.0f, 703.0f)
                        quadTo(385.0f, 760.0f, 480.0f, 760.0f)
                        quadTo(597.0f, 760.0f, 678.5f, 678.5f)
                        quadTo(760.0f, 597.0f, 760.0f, 480.0f)
                        quadTo(760.0f, 363.0f, 678.5f, 281.5f)
                        quadTo(597.0f, 200.0f, 480.0f, 200.0f)
                        quadTo(411.0f, 200.0f, 351.0f, 232.0f)
                        quadTo(291.0f, 264.0f, 250.0f, 320.0f)
                        lineTo(320.0f, 320.0f)
                        quadTo(337.0f, 320.0f, 348.5f, 331.5f)
                        quadTo(360.0f, 343.0f, 360.0f, 360.0f)
                        quadTo(360.0f, 377.0f, 348.5f, 388.5f)
                        quadTo(337.0f, 400.0f, 320.0f, 400.0f)
                        lineTo(160.0f, 400.0f)
                        quadTo(143.0f, 400.0f, 131.5f, 388.5f)
                        quadTo(120.0f, 377.0f, 120.0f, 360.0f)
                        lineTo(120.0f, 200.0f)
                        quadTo(120.0f, 183.0f, 131.5f, 171.5f)
                        quadTo(143.0f, 160.0f, 160.0f, 160.0f)
                        quadTo(177.0f, 160.0f, 188.5f, 171.5f)
                        quadTo(200.0f, 183.0f, 200.0f, 200.0f)
                        lineTo(200.0f, 254.0f)
                        quadTo(251.0f, 190.0f, 324.5f, 155.0f)
                        quadTo(398.0f, 120.0f, 480.0f, 120.0f)
                        quadTo(555.0f, 120.0f, 620.5f, 148.5f)
                        quadTo(686.0f, 177.0f, 734.5f, 225.5f)
                        quadTo(783.0f, 274.0f, 811.5f, 339.5f)
                        quadTo(840.0f, 405.0f, 840.0f, 480.0f)
                        quadTo(840.0f, 555.0f, 811.5f, 620.5f)
                        quadTo(783.0f, 686.0f, 734.5f, 734.5f)
                        quadTo(686.0f, 783.0f, 620.5f, 811.5f)
                        quadTo(555.0f, 840.0f, 480.0f, 840.0f)
                        close()
                        moveTo(520.0f, 464.0f)
                        lineTo(620.0f, 564.0f)
                        quadTo(631.0f, 575.0f, 631.0f, 592.0f)
                        quadTo(631.0f, 609.0f, 620.0f, 620.0f)
                        quadTo(609.0f, 631.0f, 592.0f, 631.0f)
                        quadTo(575.0f, 631.0f, 564.0f, 620.0f)
                        lineTo(452.0f, 508.0f)
                        quadTo(446.0f, 502.0f, 443.0f, 494.5f)
                        quadTo(440.0f, 487.0f, 440.0f, 479.0f)
                        lineTo(440.0f, 320.0f)
                        quadTo(440.0f, 303.0f, 451.5f, 291.5f)
                        quadTo(463.0f, 280.0f, 480.0f, 280.0f)
                        quadTo(497.0f, 280.0f, 508.5f, 291.5f)
                        quadTo(520.0f, 303.0f, 520.0f, 320.0f)
                        lineTo(520.0f, 464.0f)
                        close()
                    }
                }
                .build()
        return _history!!
    }

private var _history: ImageVector? = null
