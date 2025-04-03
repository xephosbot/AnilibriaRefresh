@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.internal.rememberAnimatedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val defaultAnimationSpec = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()
    val pressed by interactionSource.collectIsPressedAsState()

    androidx.compose.material3.FilterChip(
        selected = selected,
        onClick = onClick,
        label = label,
        modifier = modifier,
        leadingIcon = if (leadingIcon != null) {
            {
                AnimatedVisibility(
                    visible = selected,
                    enter = enterTransition(),
                    exit = exitTransition()
                ) {
                    leadingIcon()
                }
            }
        } else null,
        shape = shapeByInteraction(
            shape = FilterChipDefaults.shape,
            pressedShape = CircleShape,
            selectedShape = CircleShape,
            pressed = pressed,
            selected = selected,
            animationSpec = defaultAnimationSpec
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright,
            labelColor = MaterialTheme.colorScheme.onSurface,
            iconColor = MaterialTheme.colorScheme.onSurface,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = enabled,
            selected = selected,
            borderWidth = 1.dp,
            selectedBorderWidth = 0.dp
        ),
        interactionSource = interactionSource
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AssistChip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val defaultAnimationSpec = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()
    val pressed by interactionSource.collectIsPressedAsState()

    androidx.compose.material3.AssistChip(
        onClick = onClick,
        label = label,
        modifier = modifier,
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright,
            labelColor = MaterialTheme.colorScheme.onSurface,
            leadingIconContentColor = MaterialTheme.colorScheme.onSurface,
            trailingIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = AssistChipDefaults.assistChipBorder(
            enabled = enabled,
            borderWidth = 1.dp
        ),
        shape = shapeByInteraction(
            shape = FilterChipDefaults.shape,
            pressedShape = CircleShape,
            selectedShape = CircleShape,
            pressed = pressed,
            selected = false,
            animationSpec = defaultAnimationSpec
        ),
        interactionSource = interactionSource
    )
}

@Composable
private fun shapeByInteraction(
    shape: Shape,
    pressedShape: Shape,
    selectedShape: Shape,
    pressed: Boolean,
    selected: Boolean,
    animationSpec: FiniteAnimationSpec<Float>
): Shape {
    val currentShape = if (selected) selectedShape else if (pressed) pressedShape else shape
    if (currentShape is RoundedCornerShape) {
        return rememberAnimatedShape(currentShape, animationSpec)
    }
    return currentShape
}

@Stable
private fun enterTransition() = fadeIn() + expandIn(
    expandFrom = Alignment.Center,
    clip = false
) + scaleIn(initialScale = 0.4f)

@Stable
private fun exitTransition() = scaleOut(targetScale = 0.8f) + shrinkOut(
    shrinkTowards = Alignment.Center,
    clip = false
) + fadeOut()