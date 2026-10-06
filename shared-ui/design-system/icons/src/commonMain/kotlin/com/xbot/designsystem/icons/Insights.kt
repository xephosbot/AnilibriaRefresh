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
val AnilibertyIcons.Insights: ImageVector
    get() {
        if (_insights != null) {
            return _insights!!
        }
        _insights =
            Builder(
                    name = "Insights",
                    defaultWidth = 24.0.dp,
                    defaultHeight = 24.0.dp,
                    viewportWidth = 24.0f,
                    viewportHeight = 24.0f,
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
                        moveTo(3.0f, 20.0f)
                        quadTo(2.175f, 20.0f, 1.588f, 19.413f)
                        quadTo(1.0f, 18.825f, 1.0f, 18.0f)
                        quadTo(1.0f, 17.175f, 1.588f, 16.587f)
                        quadTo(2.175f, 16.0f, 3.0f, 16.0f)
                        lineTo(3.263f, 16.0f)
                        quadTo(3.375f, 16.0f, 3.5f, 16.05f)
                        lineTo(8.05f, 11.5f)
                        quadTo(8.0f, 11.375f, 8.0f, 11.262f)
                        lineTo(8.0f, 11.0f)
                        quadTo(8.0f, 10.175f, 8.588f, 9.587f)
                        quadTo(9.175f, 9.0f, 10.0f, 9.0f)
                        quadTo(10.825f, 9.0f, 11.413f, 9.587f)
                        quadTo(12.0f, 10.175f, 12.0f, 11.0f)
                        quadTo(12.0f, 11.05f, 11.95f, 11.5f)
                        lineTo(14.5f, 14.05f)
                        quadTo(14.625f, 14.0f, 14.738f, 14.0f)
                        lineTo(15.262f, 14.0f)
                        quadTo(15.375f, 14.0f, 15.5f, 14.05f)
                        lineTo(19.05f, 10.5f)
                        quadTo(19.0f, 10.375f, 19.0f, 10.262f)
                        lineTo(19.0f, 10.0f)
                        quadTo(19.0f, 9.175f, 19.587f, 8.587f)
                        quadTo(20.175f, 8.0f, 21.0f, 8.0f)
                        quadTo(21.825f, 8.0f, 22.413f, 8.587f)
                        quadTo(23.0f, 9.175f, 23.0f, 10.0f)
                        quadTo(23.0f, 10.825f, 22.413f, 11.412f)
                        quadTo(21.825f, 12.0f, 21.0f, 12.0f)
                        lineTo(20.738f, 12.0f)
                        quadTo(20.625f, 12.0f, 20.5f, 11.95f)
                        lineTo(16.95f, 15.5f)
                        quadTo(17.0f, 15.625f, 17.0f, 15.738f)
                        lineTo(17.0f, 16.0f)
                        quadTo(17.0f, 16.825f, 16.413f, 17.413f)
                        quadTo(15.825f, 18.0f, 15.0f, 18.0f)
                        quadTo(14.175f, 18.0f, 13.588f, 17.413f)
                        quadTo(13.0f, 16.825f, 13.0f, 16.0f)
                        lineTo(13.0f, 15.738f)
                        quadTo(13.0f, 15.625f, 13.05f, 15.5f)
                        lineTo(10.5f, 12.95f)
                        quadTo(10.375f, 13.0f, 10.262f, 13.0f)
                        lineTo(10.0f, 13.0f)
                        quadTo(9.95f, 13.0f, 9.5f, 12.95f)
                        lineTo(4.95f, 17.5f)
                        quadTo(5.0f, 17.625f, 5.0f, 17.738f)
                        lineTo(5.0f, 18.0f)
                        quadTo(5.0f, 18.825f, 4.412f, 19.413f)
                        quadTo(3.825f, 20.0f, 3.0f, 20.0f)
                        close()
                        moveTo(4.0f, 9.975f)
                        lineTo(3.375f, 8.625f)
                        lineTo(2.025f, 8.0f)
                        lineTo(3.375f, 7.375f)
                        lineTo(4.0f, 6.025f)
                        lineTo(4.625f, 7.375f)
                        lineTo(5.975f, 8.0f)
                        lineTo(4.625f, 8.625f)
                        close()
                        moveTo(15.0f, 9.0f)
                        lineTo(14.05f, 6.95f)
                        lineTo(12.0f, 6.0f)
                        lineTo(14.05f, 5.05f)
                        lineTo(15.0f, 3.0f)
                        lineTo(15.95f, 5.05f)
                        lineTo(18.0f, 6.0f)
                        lineTo(15.95f, 6.95f)
                        close()
                    }
                }
                .build()
        return _insights!!
    }

private var _insights: ImageVector? = null
