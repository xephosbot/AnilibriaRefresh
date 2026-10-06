package com.xbot.title.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.ToggleButtonShapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.Favorite
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.formatters.formatCompact
import com.xbot.resources.Res
import com.xbot.resources.release_details_favorite
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun FavoriteButton(
    checked: Boolean,
    count: Int,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    ToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.size(FavoriteButtonDefaults.Size),
        enabled = enabled,
        shapes = FavoriteButtonDefaults.Shapes,
        colors = ToggleButtonDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            checkedContainerColor = MaterialTheme.colorScheme.primary,
            checkedContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = if (checked) {
                    AnilibertyIcons.Filled.Favorite
                } else {
                    AnilibertyIcons.Outlined.Favorite
                },
                contentDescription = stringResource(Res.string.release_details_favorite)
            )
            Text(
                text = count.formatCompact(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

internal object FavoriteButtonDefaults {
    val Size = PlayButtonDefaults.Height
    val Shapes = ToggleButtonShapes(
        shape = RoundedCornerShape(20.dp),
        pressedShape = RoundedCornerShape(14.dp),
        checkedShape = CircleShape
    )
}

@AnilibertyPreview
@Composable
private fun FavoriteButtonPreview() {
    var checked by remember { mutableStateOf(false) }

    FavoriteButton(
        checked = checked,
        count = 25_480,
        onCheckedChange = { checked = it }
    )
}
