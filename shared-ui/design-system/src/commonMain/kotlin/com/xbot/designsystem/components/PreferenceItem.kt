package com.xbot.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.Check
import com.xbot.designsystem.icons.Close
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape
import com.xbot.designsystem.utils.AnilibriaPreview

@Composable
fun PreferenceItem(
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    selected: Boolean = false,
    colors: PreferenceItemColors = ExpressivePreferenceItemDefaults.colors(),
    shape: ExpressiveShape = ExpressivePreferenceItemDefaults.shape(),
    interactionSource: MutableInteractionSource? = null,
    onClick: (() -> Unit)? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val containerColor by animateColorAsState(
        targetValue = if (selected) colors.selectedContainerColor else colors.containerColor,
        label = "PreferenceItemContainerColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) colors.selectedContentColor else colors.contentColor,
        label = "PreferenceItemContentColor"
    )

    Surface(
        modifier = modifier,
        shape = shape.shapeForInteraction(pressed, selected),
        color = containerColor,
        contentColor = contentColor,
        onClick = onClick ?: {},
        enabled = onClick != null,
        interactionSource = interactionSource
    ) {
        ListItem(
            headlineContent = headlineContent,
            supportingContent = supportingContent,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
                headlineColor = contentColor,
                leadingIconColor = contentColor,
                overlineColor = contentColor,
                supportingColor = contentColor.copy(alpha = 0.7f),
                trailingIconColor = contentColor
            )
        )
    }
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
    fun shape(): ExpressiveShape {
        return RoundedCornerExpressiveShape(
            shape = RoundedCornerShape(0.dp),
            pressedShape = MaterialTheme.shapes.large,
            selectedShape = MaterialTheme.shapes.large,
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
        )
    }

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceBright,
        contentColor: Color = contentColorFor(containerColor),
        selectedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
        selectedContentColor: Color = contentColorFor(selectedContainerColor)
    ): PreferenceItemColors = PreferenceItemColors(
        containerColor = containerColor,
        contentColor = contentColor,
        selectedContainerColor = selectedContainerColor,
        selectedContentColor = selectedContentColor
    )
}

@Immutable
data class PreferenceItemColors(
    val containerColor: Color,
    val contentColor: Color,
    val selectedContainerColor: Color,
    val selectedContentColor: Color
)

@Preview
@Composable
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
