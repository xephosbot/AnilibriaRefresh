package com.xbot.anilibriarefresh.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("UnusedReceiverParameter")
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
				fill = SolidColor(Color(0x00000000)),
				stroke = SolidColor(Color(0xFFffffff)),
				strokeLineWidth = 2.0f,
				strokeLineCap = Round,
				strokeLineJoin = StrokeJoin.Companion.Round,
				strokeLineMiter = 4.0f,
				pathFillType = NonZero
			) {
				moveTo(4.0f, 11.4522f)
				verticalLineTo(16.8002f)
				curveTo(4.0f, 17.9203f, 4.0f, 18.4807f, 4.218f, 18.9086f)
				curveTo(4.4097f, 19.2849f, 4.7155f, 19.5906f, 5.0918f, 19.7823f)
				curveTo(5.5192f, 20.0001f, 6.079f, 20.0001f, 7.1969f, 20.0001f)
				horizontalLineTo(16.8031f)
				curveTo(17.921f, 20.0001f, 18.48f, 20.0001f, 18.9074f, 19.7823f)
				curveTo(19.2837f, 19.5906f, 19.5905f, 19.2849f, 19.7822f, 18.9086f)
				curveTo(20.0f, 18.4811f, 20.0f, 17.9216f, 20.0f, 16.8037f)
				verticalLineTo(11.4522f)
				curveTo(20.0f, 10.9179f, 19.9995f, 10.6506f, 19.9346f, 10.4019f)
				curveTo(19.877f, 10.1816f, 19.7825f, 9.9731f, 19.6546f, 9.7846f)
				curveTo(19.5102f, 9.572f, 19.3096f, 9.3957f, 18.9074f, 9.0438f)
				lineTo(14.1074f, 4.8438f)
				curveTo(13.3608f, 4.1905f, 12.9875f, 3.8641f, 12.5674f, 3.7398f)
				curveTo(12.1972f, 3.6304f, 11.8026f, 3.6304f, 11.4324f, 3.7398f)
				curveTo(11.0126f, 3.864f, 10.6398f, 4.1901f, 9.8944f, 4.8424f)
				lineTo(5.0928f, 9.0438f)
				curveTo(4.6906f, 9.3957f, 4.49f, 9.572f, 4.3457f, 9.7846f)
				curveTo(4.2178f, 9.9731f, 4.1226f, 10.1816f, 4.065f, 10.4019f)
				curveTo(4.0f, 10.6506f, 4.0f, 10.9179f, 4.0f, 11.4522f)
				close()
			}
		}.build()
		return _house!!
	}

private var _house: ImageVector? = null
