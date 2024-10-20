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
val AnilibriaIcons.Outlined.Heart: ImageVector
	get() {
		if (_heart != null) {
			return _heart!!
		}
		_heart = ImageVector.Builder(
            name = "Outlined.Heart",
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
				moveTo(12.0f, 7.6943f)
				curveTo(10.0f, 2.9999f, 3.0f, 3.4999f, 3.0f, 9.4999f)
				curveTo(3.0f, 15.4999f, 12.0f, 20.5001f, 12.0f, 20.5001f)
				curveTo(12.0f, 20.5001f, 21.0f, 15.4999f, 21.0f, 9.4999f)
				curveTo(21.0f, 3.4999f, 14.0f, 2.9999f, 12.0f, 7.6943f)
				close()
			}
		}.build()
		return _heart!!
	}

private var _heart: ImageVector? = null
