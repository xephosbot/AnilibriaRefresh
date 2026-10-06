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
val AnilibertyIcons.MyAnimeListLogo: ImageVector
    get() {
        if (_myAnimeListLogo != null) {
            return _myAnimeListLogo!!
        }
        _myAnimeListLogo =
            Builder(
                    name = "MyAnimeListLogo",
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
                        moveTo(14.921f, 6.479f)
                        curveTo(14.101f, 6.479f, 11.238f, 6.479f, 9.974f, 9.635f)
                        curveTo(9.312f, 11.287f, 8.988f, 14.447f, 10.85f, 17.521f)
                        lineTo(12.784f, 16.111f)
                        curveTo(12.784f, 16.111f, 12.017f, 15.016f, 11.701f, 12.92f)
                        lineTo(14.598f, 12.92f)
                        lineTo(14.62f, 16.11f)
                        lineTo(17.224f, 16.11f)
                        lineTo(17.224f, 8.835f)
                        lineTo(14.643f, 8.835f)
                        lineTo(14.643f, 10.878f)
                        lineTo(12.183f, 10.855f)
                        curveTo(12.183f, 10.855f, 12.596f, 8.447f, 15.06f, 8.519f)
                        lineTo(17.514f, 8.519f)
                        lineTo(16.942f, 6.479f)
                        close()
                        moveTo(0.0f, 6.528f)
                        lineTo(0.0f, 16.152f)
                        lineTo(2.348f, 16.152f)
                        lineTo(2.348f, 10.312f)
                        lineTo(4.379f, 12.976f)
                        lineTo(6.426f, 10.324f)
                        lineTo(6.426f, 16.152f)
                        lineTo(8.762f, 16.152f)
                        lineTo(8.762f, 6.528f)
                        lineTo(6.437f, 6.528f)
                        lineTo(4.368f, 9.474f)
                        lineTo(2.31f, 6.528f)
                        close()
                        moveTo(18.447f, 6.55f)
                        lineTo(18.447f, 16.133f)
                        lineTo(23.469f, 16.133f)
                        lineTo(24.0f, 14.09f)
                        lineTo(20.768f, 14.09f)
                        lineTo(20.768f, 6.55f)
                        close()
                    }
                }
                .build()
        return _myAnimeListLogo!!
    }

private var _myAnimeListLogo: ImageVector? = null
