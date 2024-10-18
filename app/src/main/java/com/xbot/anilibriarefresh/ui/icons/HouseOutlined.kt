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
val AnilibriaIcons.Outlined.House: ImageVector
	get() {
		if (_house != null) {
			return _house!!
		}
		_house = ImageVector.Builder(
            name = "Outlined.House",
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
				moveTo(4f, 11.4522f)
				verticalLineTo(16.8002f)
				curveTo(40f, 17.92030f, 40f, 18.48070f, 4.2180f, 18.90860f)
				curveTo(4.40970f, 19.28490f, 4.71550f, 19.59060f, 5.09180f, 19.78230f)
				curveTo(5.51920f, 20.00010f, 6.0790f, 20.00010f, 7.19690f, 20.00010f)
				horizontalLineTo(16.8031f)
				curveTo(17.9210f, 20.00010f, 18.480f, 20.00010f, 18.90740f, 19.78230f)
				curveTo(19.28370f, 19.59060f, 19.59050f, 19.28490f, 19.78220f, 18.90860f)
				curveTo(200f, 18.48110f, 200f, 17.92160f, 200f, 16.80370f)
				verticalLineTo(11.4522f)
				curveTo(200f, 10.91790f, 19.99950f, 10.65060f, 19.93460f, 10.40190f)
				curveTo(19.8770f, 10.18160f, 19.78250f, 9.97310f, 19.65460f, 9.78460f)
				curveTo(19.51020f, 9.5720f, 19.30960f, 9.39570f, 18.90740f, 9.04380f)
				lineTo(14.1074f, 4.84383f)
				curveTo(13.36080f, 4.19050f, 12.98750f, 3.86410f, 12.56740f, 3.73980f)
				curveTo(12.19720f, 3.63030f, 11.80260f, 3.63030f, 11.43240f, 3.73980f)
				curveTo(11.01260f, 3.8640f, 10.63980f, 4.19010f, 9.89440f, 4.84240f)
				lineTo(5.09277f, 9.04383f)
				curveTo(4.69060f, 9.39570f, 4.490f, 9.5720f, 4.34570f, 9.78460f)
				curveTo(4.21780f, 9.97310f, 4.12260f, 10.18160f, 4.0650f, 10.40190f)
				curveTo(40f, 10.65060f, 40f, 10.91790f, 40f, 11.45220f)
				close()
			}
		}.build()
		return _house!!
	}

private var _house: ImageVector? = null
