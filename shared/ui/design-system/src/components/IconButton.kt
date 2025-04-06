package com.xbot.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(color),
    shape: Shape = CircleShape,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(30.dp, 38.dp)
            .clip(shape)
            .background(color)
            .clickable(
                onClick = onClick,
                role = Role.Button
            ),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

@Composable
fun LabeledIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
    interactionSource: MutableInteractionSource? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor.copy(alpha = 0.8f)) {
            OutlinedIconButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick,
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceBright,
                    contentColor = LocalContentColor.current
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.0f)
                ),
                interactionSource = interactionSource
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}