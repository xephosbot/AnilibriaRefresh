package com.xbot.anilibriarefresh.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/*
* Converted using https://composables.com/svgtocompose
*/
val AnilibriaIcons.Filled.Heart: ImageVector
	get() {
		if (_heart != null) {
			return _heart!!
		}
		_heart = ImageVector.Builder(
            name = "Filled.Heart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
			path(
    			fill = SolidColor(Color(0xFFFFFFFF)),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(12f, 7.69428f)
				curveTo(100f, 2.99980f, 30f, 3.49980f, 30f, 9.49990f)
				curveTo(30f, 15.49990f, 120f, 20.50010f, 120f, 20.50010f)
				curveTo(120f, 20.50010f, 210f, 15.49990f, 210f, 9.49990f)
				curveTo(210f, 3.49980f, 140f, 2.99980f, 120f, 7.69430f)
				close()
			}
		}.build()
		return _heart!!
	}

private var _heart: ImageVector? = null
