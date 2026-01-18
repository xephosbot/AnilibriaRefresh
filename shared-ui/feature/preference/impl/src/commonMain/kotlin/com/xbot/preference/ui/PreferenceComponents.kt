package com.xbot.preference.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.Check
import com.xbot.designsystem.icons.Close

@Composable
fun PreferenceItem(
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceBright),
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        ),
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        colors = colors
    )
}

@Composable
fun SwitchPreferenceItem(
    headlineContent: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    badges: @Composable (() -> Unit)? = null,
) {
    PreferenceItem(
        modifier = modifier,
        headlineContent = headlineContent,
        supportingContent = {
            Column {
                if (supportingContent != null) {
                    supportingContent()
                }
                if (badges != null) {
                    if (supportingContent != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    badges()
                }
            }
        },
        leadingContent = leadingContent,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                thumbContent = {
                    if (checked) {
                        Icon(
                            imageVector = AnilibriaIcons.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    } else {
                        Icon(
                            imageVector = AnilibriaIcons.Close,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    }
                }
            )
        },
        onClick = { onCheckedChange(!checked) }
    )
}

@Composable
fun ExperimentalPill(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(color),
    shape: Shape = CircleShape,
    content: @Composable () -> Unit = {
        Text(
            text = "Experimental",
            style = MaterialTheme.typography.bodySmall
        )
    }
) {
    Surface(
        modifier = modifier,
        color = color,
        contentColor = contentColor,
        shape = shape
    ) {
        Box(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}
