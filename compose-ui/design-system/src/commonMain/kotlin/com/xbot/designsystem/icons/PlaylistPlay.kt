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
val AnilibriaIcons.PlaylistPlay: ImageVector
    get() {
        if (_playlistPlay != null) {
            return _playlistPlay!!
        }
        _playlistPlay =
            Builder(
                    name = "PlaylistPlay",
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
                        moveTo(160.0f, 640.0f)
                        quadTo(143.0f, 640.0f, 131.5f, 628.5f)
                        quadTo(120.0f, 617.0f, 120.0f, 600.0f)
                        quadTo(120.0f, 583.0f, 131.5f, 571.5f)
                        quadTo(143.0f, 560.0f, 160.0f, 560.0f)
                        lineTo(400.0f, 560.0f)
                        quadTo(417.0f, 560.0f, 428.5f, 571.5f)
                        quadTo(440.0f, 583.0f, 440.0f, 600.0f)
                        quadTo(440.0f, 617.0f, 428.5f, 628.5f)
                        quadTo(417.0f, 640.0f, 400.0f, 640.0f)
                        lineTo(160.0f, 640.0f)
                        close()
                        moveTo(160.0f, 480.0f)
                        quadTo(143.0f, 480.0f, 131.5f, 468.5f)
                        quadTo(120.0f, 457.0f, 120.0f, 440.0f)
                        quadTo(120.0f, 423.0f, 131.5f, 411.5f)
                        quadTo(143.0f, 400.0f, 160.0f, 400.0f)
                        lineTo(560.0f, 400.0f)
                        quadTo(577.0f, 400.0f, 588.5f, 411.5f)
                        quadTo(600.0f, 423.0f, 600.0f, 440.0f)
                        quadTo(600.0f, 457.0f, 588.5f, 468.5f)
                        quadTo(577.0f, 480.0f, 560.0f, 480.0f)
                        lineTo(160.0f, 480.0f)
                        close()
                        moveTo(160.0f, 320.0f)
                        quadTo(143.0f, 320.0f, 131.5f, 308.5f)
                        quadTo(120.0f, 297.0f, 120.0f, 280.0f)
                        quadTo(120.0f, 263.0f, 131.5f, 251.5f)
                        quadTo(143.0f, 240.0f, 160.0f, 240.0f)
                        lineTo(560.0f, 240.0f)
                        quadTo(577.0f, 240.0f, 588.5f, 251.5f)
                        quadTo(600.0f, 263.0f, 600.0f, 280.0f)
                        quadTo(600.0f, 297.0f, 588.5f, 308.5f)
                        quadTo(577.0f, 320.0f, 560.0f, 320.0f)
                        lineTo(160.0f, 320.0f)
                        close()
                        moveTo(671.0f, 819.0f)
                        quadTo(666.0f, 822.0f, 661.0f, 822.0f)
                        quadTo(656.0f, 822.0f, 651.0f, 820.0f)
                        quadTo(646.0f, 818.0f, 643.0f, 813.5f)
                        quadTo(640.0f, 809.0f, 640.0f, 803.0f)
                        lineTo(640.0f, 557.0f)
                        quadTo(640.0f, 551.0f, 643.0f, 546.5f)
                        quadTo(646.0f, 542.0f, 651.0f, 540.0f)
                        quadTo(656.0f, 538.0f, 661.0f, 538.0f)
                        quadTo(666.0f, 538.0f, 671.0f, 541.0f)
                        lineTo(855.0f, 663.0f)
                        quadTo(860.0f, 666.0f, 862.0f, 670.5f)
                        quadTo(864.0f, 675.0f, 864.0f, 680.0f)
                        quadTo(864.0f, 685.0f, 862.0f, 689.5f)
                        quadTo(860.0f, 694.0f, 855.0f, 697.0f)
                        lineTo(671.0f, 819.0f)
                        close()
                    }
                }
                .build()
        return _playlistPlay!!
    }

private var _playlistPlay: ImageVector? = null
