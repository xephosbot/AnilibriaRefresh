package com.xbot.designsystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.Check
import com.xbot.designsystem.icons.Close
import com.xbot.designsystem.utils.AnilibriaPreview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceItem(
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    selected: Boolean = false,
    colors: ListItemColors = ExpressivePreferenceItemDefaults.colors(),
    shapes: ListItemShapes = ExpressivePreferenceItemDefaults.shapes(),
    interactionSource: MutableInteractionSource? = null,
    onClick: (() -> Unit)? = null,
) {
    val validOnClick = onClick ?: {}
    val enabled = onClick != null

    ListItem(
        selected = selected,
        onClick = validOnClick,
        modifier = modifier,
        enabled = enabled,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        supportingContent = supportingContent,
        colors = colors,
        shapes = shapes,
        interactionSource = interactionSource,
        content = headlineContent
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SwitchPreferenceItem(
    headlineContent: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    badges: @Composable (() -> Unit)? = null,
    selected: Boolean = false,
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
        onClick = { onCheckedChange(!checked) },
        selected = selected
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressivePreferenceItemDefaults {
    @Composable
    fun shapes(
        shape: Shape = RoundedCornerShape(0.dp),
        pressedShape: Shape = MaterialTheme.shapes.large,
        selectedShape: Shape = MaterialTheme.shapes.large,
    ): ListItemShapes {
        return ListItemDefaults.shapes(
            shape = shape,
            pressedShape = pressedShape,
            selectedShape = selectedShape,
        )
    }

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceBright,
        contentColor: Color = contentColorFor(containerColor),
        supportingContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        selectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        selectedContentColor: Color = contentColorFor(selectedContainerColor)
    ): ListItemColors {
        return ListItemDefaults.colors(
            containerColor = containerColor,
            contentColor = contentColor,
            leadingContentColor = contentColor,
            overlineContentColor = contentColor,
            supportingContentColor = supportingContentColor,
            trailingContentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor,
            disabledLeadingContentColor = contentColor,
            disabledOverlineContentColor = contentColor,
            disabledSupportingContentColor = supportingContentColor,
            disabledTrailingContentColor = contentColor,
            selectedContainerColor = selectedContainerColor,
            selectedContentColor = selectedContentColor,
            selectedLeadingContentColor = selectedContentColor,
            selectedOverlineContentColor = selectedContentColor,
            selectedSupportingContentColor = selectedContentColor.copy(alpha = 0.7f),
            selectedTrailingContentColor = selectedContentColor
        )
    }
}

@Preview
@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun PreferenceItemPreview() {
    AnilibriaPreview {
        Column {
            PreferenceItem(
                headlineContent = { Text("Preference Item") },
                supportingContent = { Text("Supporting content") },
                selected = false,
                onClick = {}
            )
            Spacer(Modifier.height(8.dp))
            PreferenceItem(
                headlineContent = { Text("Selected Item") },
                supportingContent = { Text("Supporting content") },
                selected = true,
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun SwitchPreferenceItemPreview() {
    AnilibriaPreview {
        Column {
            SwitchPreferenceItem(
                headlineContent = { Text("Switch Preference") },
                checked = true,
                onCheckedChange = {}
            )
            Spacer(Modifier.height(8.dp))
            SwitchPreferenceItem(
                headlineContent = { Text("Unchecked Preference") },
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}
