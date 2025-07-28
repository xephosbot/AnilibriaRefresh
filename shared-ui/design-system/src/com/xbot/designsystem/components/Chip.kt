package com.xbot.designsystem.components

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    shape: ExpressiveShape = ExpressiveChipDefaults.shape(),
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val fadeInSpec = MaterialTheme.motionScheme.slowEffectsSpec<Float>()
    val fadeOutSpec = MaterialTheme.motionScheme.fastEffectsSpec<Float>()
    val expandSpec = MaterialTheme.motionScheme.fastSpatialSpec<IntSize>()
    val shrinkSpec = MaterialTheme.motionScheme.defaultEffectsSpec<IntSize>()

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = label,
        modifier = modifier,
        leadingIcon = if (leadingIcon != null) {
            {
                AnimatedVisibility(
                    visible = selected,
                    enter =
                        expandHorizontally(
                            animationSpec = expandSpec,
                            expandFrom = Alignment.Start,
                        ) + fadeIn(animationSpec = fadeInSpec),
                    exit =
                        shrinkHorizontally(
                            animationSpec = shrinkSpec,
                            shrinkTowards = Alignment.Start,
                        ) + fadeOut(animationSpec = fadeOutSpec),
                ) {
                    val leadingContentRetainedState = rememberRetainedState(targetValue = leadingIcon)

                    Box(
                        contentAlignment = Alignment.Center,
                        content = {
                            // Read from retained state
                            leadingContentRetainedState.value?.invoke()
                        }
                    )
                }
            }
        } else null,
        shape = shape.shapeForInteraction(pressed, selected),
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
    shape: ExpressiveShape = ExpressiveChipDefaults.shape(),
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    AssistChip(
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
        shape = shape.shapeForInteraction(pressed, false),
        interactionSource = interactionSource
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressiveChipDefaults {
    private var _shape: ExpressiveShape? = null

    @Composable
    fun shape(): ExpressiveShape {
        return _shape ?: RoundedCornerExpressiveShape(
            shape = FilterChipDefaults.shape,
            pressedShape = CircleShape,
            selectedShape = CircleShape,
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
        ).also { _shape = it }
    }
}

@Composable
private fun <T> rememberRetainedState(targetValue: T?): State<T?> {
    val retainedState = remember { mutableStateOf(targetValue) }
    if (targetValue != null) {
        retainedState.value = targetValue
    }
    return retainedState
}