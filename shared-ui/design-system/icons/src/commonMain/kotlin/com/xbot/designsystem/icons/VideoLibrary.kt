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
val AnilibertyIcons.VideoLibrary: ImageVector
    get() {
        if (_videoLibrary != null) {
            return _videoLibrary!!
        }
        _videoLibrary =
            Builder(
                name = "VideoLibrary",
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
                        moveTo(701.0f, 425.0f)
                        quadTo(715.0f, 416.0f, 715.0f, 400.0f)
                        quadTo(715.0f, 384.0f, 701.0f, 375.0f)
                        lineTo(506.0f, 250.0f)
                        quadTo(491.0f, 240.0f, 475.5f, 248.5f)
                        quadTo(460.0f, 257.0f, 460.0f, 275.0f)
                        lineTo(460.0f, 525.0f)
                        quadTo(460.0f, 543.0f, 475.5f, 551.5f)
                        quadTo(491.0f, 560.0f, 506.0f, 550.0f)
                        lineTo(701.0f, 425.0f)
                        close()
                        moveTo(320.0f, 720.0f)
                        quadTo(287.0f, 720.0f, 263.5f, 696.5f)
                        quadTo(240.0f, 673.0f, 240.0f, 640.0f)
                        lineTo(240.0f, 160.0f)
                        quadTo(240.0f, 127.0f, 263.5f, 103.5f)
                        quadTo(287.0f, 80.0f, 320.0f, 80.0f)
                        lineTo(800.0f, 80.0f)
                        quadTo(833.0f, 80.0f, 856.5f, 103.5f)
                        quadTo(880.0f, 127.0f, 880.0f, 160.0f)
                        lineTo(880.0f, 640.0f)
                        quadTo(880.0f, 673.0f, 856.5f, 696.5f)
                        quadTo(833.0f, 720.0f, 800.0f, 720.0f)
                        lineTo(320.0f, 720.0f)
                        close()
                        moveTo(320.0f, 640.0f)
                        lineTo(800.0f, 640.0f)
                        lineTo(800.0f, 160.0f)
                        lineTo(320.0f, 160.0f)
                        lineTo(320.0f, 640.0f)
                        close()
                        moveTo(160.0f, 880.0f)
                        quadTo(127.0f, 880.0f, 103.5f, 856.5f)
                        quadTo(80.0f, 833.0f, 80.0f, 800.0f)
                        lineTo(80.0f, 280.0f)
                        quadTo(80.0f, 263.0f, 91.5f, 251.5f)
                        quadTo(103.0f, 240.0f, 120.0f, 240.0f)
                        quadTo(137.0f, 240.0f, 148.5f, 251.5f)
                        quadTo(160.0f, 263.0f, 160.0f, 280.0f)
                        lineTo(160.0f, 800.0f)
                        lineTo(680.0f, 800.0f)
                        quadTo(697.0f, 800.0f, 708.5f, 811.5f)
                        quadTo(720.0f, 823.0f, 720.0f, 840.0f)
                        quadTo(720.0f, 857.0f, 708.5f, 868.5f)
                        quadTo(697.0f, 880.0f, 680.0f, 880.0f)
                        lineTo(160.0f, 880.0f)
                        close()
                        moveTo(320.0f, 160.0f)
                        lineTo(320.0f, 640.0f)
                        lineTo(320.0f, 160.0f)
                        close()
                    }
                }
                .build()
        return _videoLibrary!!
    }

private var _videoLibrary: ImageVector? = null
