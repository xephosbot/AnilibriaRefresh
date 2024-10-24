package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xbot.anilibriarefresh.R

@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    colorStops: Array<Pair<Float, Color>>? = null
) {
    Row(
        modifier
            .background(
                if (colorStops == null) Brush.horizontalGradient(colorStops = buttonDefaultColorStops)
                else Brush.horizontalGradient(colorStops = colorStops)
            )
            .clickable { onClick() }
            .padding(ButtonComponentPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(R.string.content_desc_btn_component)
            )
            Spacer(Modifier.padding(end = ButtonComponentPadding))
        }
        Text(
            text = text
        )
    }
}

private val ButtonComponentPadding = 12.dp

private val buttonDefaultColorStops @Composable
get() = arrayOf(
    0.0f to Color.Transparent,
    0.0f to Color.Transparent,
    1f to MaterialTheme.colorScheme.surface
)

