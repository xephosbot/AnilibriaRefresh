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
val AnilibriaIcons.Outlined.Search: ImageVector
	get() {
		if (_search != null) {
			return _search!!
		}
		_search = ImageVector.Builder(
            name = "Outlined.Search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFFFFFFFF)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(15f, 15f)
				lineTo(21f, 21f)
				moveTo(10f, 17f)
				curveTo(6.1340f, 170f, 30f, 13.8660f, 30f, 100f)
				curveTo(30f, 6.1340f, 6.1340f, 30f, 100f, 30f)
				curveTo(13.8660f, 30f, 170f, 6.1340f, 170f, 100f)
				curveTo(170f, 13.8660f, 13.8660f, 170f, 100f, 170f)
				close()
			}
		}.build()
		return _search!!
	}

private var _search: ImageVector? = null
