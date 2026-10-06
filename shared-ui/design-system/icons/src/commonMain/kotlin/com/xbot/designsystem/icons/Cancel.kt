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
val AnilibertyIcons.Cancel: ImageVector
    get() {
        if (_cancel != null) {
            return _cancel!!
        }
        _cancel =
            Builder(
                    name = "Cancel",
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
                        lineTo(596.0f, 652.0f)
                        quadTo(607.0f, 663.0f, 624.0f, 663.0f)
                        quadTo(641.0f, 663.0f, 652.0f, 652.0f)
                        quadTo(663.0f, 641.0f, 663.0f, 624.0f)
                        quadTo(663.0f, 607.0f, 652.0f, 596.0f)
                        lineTo(536.0f, 480.0f)
                        lineTo(652.0f, 364.0f)
                        quadTo(663.0f, 353.0f, 663.0f, 336.0f)
                        quadTo(663.0f, 319.0f, 652.0f, 308.0f)
                        quadTo(641.0f, 297.0f, 624.0f, 297.0f)
                        quadTo(607.0f, 297.0f, 596.0f, 308.0f)
                        lineTo(480.0f, 424.0f)
                        lineTo(364.0f, 308.0f)
                        quadTo(353.0f, 297.0f, 336.0f, 297.0f)
                        quadTo(319.0f, 297.0f, 308.0f, 308.0f)
                        quadTo(297.0f, 319.0f, 297.0f, 336.0f)
                        quadTo(297.0f, 353.0f, 308.0f, 364.0f)
                        lineTo(424.0f, 480.0f)
                        lineTo(308.0f, 596.0f)
                        quadTo(297.0f, 607.0f, 297.0f, 624.0f)
                        quadTo(297.0f, 641.0f, 308.0f, 652.0f)
                        quadTo(319.0f, 663.0f, 336.0f, 663.0f)
                        quadTo(353.0f, 663.0f, 364.0f, 652.0f)
                        lineTo(480.0f, 536.0f)
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
                        moveTo(480.0f, 800.0f)
                        quadTo(614.0f, 800.0f, 707.0f, 707.0f)
                        quadTo(800.0f, 614.0f, 800.0f, 480.0f)
                        quadTo(800.0f, 346.0f, 707.0f, 253.0f)
                        quadTo(614.0f, 160.0f, 480.0f, 160.0f)
                        quadTo(346.0f, 160.0f, 253.0f, 253.0f)
                        quadTo(160.0f, 346.0f, 160.0f, 480.0f)
                        quadTo(160.0f, 614.0f, 253.0f, 707.0f)
                        quadTo(346.0f, 800.0f, 480.0f, 800.0f)
                        close()
                        moveTo(480.0f, 480.0f)
                        close()
                    }
                }
                .build()
        return _cancel!!
    }

private var _cancel: ImageVector? = null
