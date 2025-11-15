@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.designsystem.components

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonDefaults.SmallContainerHeight
import androidx.compose.material3.SplitButtonDefaults.TrailingButtonStateLayerAlpha
import androidx.compose.material3.SplitButtonDefaults.trailingButtonContentPaddingFor
import androidx.compose.material3.SplitButtonShapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.internal.ProvideContentColorTextStyle
import androidx.compose.material3.internal.rememberAnimatedShape
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.tokens.MotionSchemeKeyTokens
import androidx.compose.material3.value
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastFirst
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowDropDown
import com.xbot.designsystem.icons.PlayArrow
import com.xbot.designsystem.utils.PreviewContainer
import com.xbot.resources.Res
import com.xbot.resources.button_watch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MediumSplitButton(
    onLeadingClick: () -> Unit,
    trailingChecked: Boolean,
    onTrailingCheckedChange: (Boolean) -> Unit,
    leadingContent: @Composable RowScope.() -> Unit,
    trailingContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = com.xbot.designsystem.components.SplitButtonDefaults.colors(),
    spacing: Dp = SplitButtonDefaults.Spacing,
    dropdownContent: @Composable ColumnScope.() -> Unit,
) {
    SplitButtonLayout(
        leadingButton = {
            SplitButtonDefaults.LeadingButton(
                modifier = modifier
                    .fillMaxWidth()
                    .height(SplitButtonDefaults.MediumContainerHeight),
                shapes = SplitButtonDefaults
                    .leadingButtonShapesFor(SplitButtonDefaults.MediumContainerHeight),
                colors = colors,
                onClick = onLeadingClick,
                contentPadding = SplitButtonDefaults
                    .leadingButtonContentPaddingFor(SplitButtonDefaults.MediumContainerHeight),
                content = {
                    ProvideTextStyle(
                        LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        leadingContent()
                    }
                }
            )
        },
        trailingButton = {
            Box {
                com.xbot.designsystem.components.SplitButtonDefaults.TrailingButton(
                    checked = trailingChecked,
                    onCheckedChange = onTrailingCheckedChange,
                    modifier = Modifier
                        .height(SplitButtonDefaults.MediumContainerHeight),
                    shapes = SplitButtonDefaults
                        .trailingButtonShapesFor(SplitButtonDefaults.MediumContainerHeight),
                    colors = colors,
                    contentPadding = SplitButtonDefaults
                        .trailingButtonContentPaddingFor(SplitButtonDefaults.MediumContainerHeight),
                    content = trailingContent,
                )
                DropdownMenu(
                    expanded = trailingChecked,
                    onDismissRequest = { onTrailingCheckedChange(false) },
                    content = dropdownContent
                )
            }
        },
        modifier = modifier,
        spacing = spacing
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MediumSplitButton(
    onLeadingClick: () -> Unit,
    onTrailingClick: () -> Unit,
    leadingContent: @Composable RowScope.() -> Unit,
    trailingContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = com.xbot.designsystem.components.SplitButtonDefaults.colors(),
    spacing: Dp = SplitButtonDefaults.Spacing,
) {
    SplitButtonLayout(
        leadingButton = {
            SplitButtonDefaults.LeadingButton(
                modifier = modifier
                    .fillMaxWidth()
                    .height(SplitButtonDefaults.MediumContainerHeight),
                shapes = SplitButtonDefaults
                    .leadingButtonShapesFor(SplitButtonDefaults.MediumContainerHeight),
                colors = colors,
                onClick = onLeadingClick,
                contentPadding = SplitButtonDefaults
                    .leadingButtonContentPaddingFor(SplitButtonDefaults.MediumContainerHeight),
                content = {
                    ProvideTextStyle(
                        LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        leadingContent()
                    }
                }
            )
        },
        trailingButton = {
            SplitButtonDefaults.TrailingButton(
                modifier = Modifier
                    .height(SplitButtonDefaults.MediumContainerHeight),
                shapes = SplitButtonDefaults
                    .trailingButtonShapesFor(SplitButtonDefaults.MediumContainerHeight),
                colors = colors,
                onClick = onTrailingClick,
                contentPadding = SplitButtonDefaults
                    .trailingButtonContentPaddingFor(SplitButtonDefaults.MediumContainerHeight),
                content = trailingContent
            )
        },
        modifier = modifier,
        spacing = spacing
    )
}

@ExperimentalMaterial3ExpressiveApi
@Composable
private fun SplitButtonLayout(
    leadingButton: @Composable () -> Unit,
    trailingButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    spacing: Dp = SplitButtonDefaults.Spacing,
) {
    Layout(
        {
            // Override min component size enforcement to avoid create extra padding internally
            // Enforce it on the parent instead
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                Box(
                    modifier = Modifier.layoutId(LeadingButtonLayoutId),
                    contentAlignment = Alignment.Center,
                    content = { leadingButton() }
                )
                Box(
                    modifier = Modifier.layoutId(TrailingButtonLayoutId),
                    contentAlignment = Alignment.Center,
                    content = { trailingButton() }
                )
            }
        },
        modifier.minimumInteractiveComponentSize(),
        measurePolicy = { measurables, constraints ->
            val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            val trailingMeasurable = measurables
                .fastFirst { it.layoutId == TrailingButtonLayoutId }

            val trailingPlaceable = trailingMeasurable.measure(
                looseConstraints.copy(
                    minHeight = constraints.minHeight,
                    maxHeight = constraints.maxHeight
                )
            )

            val availableForLeading =
                (constraints.maxWidth - trailingPlaceable.width - spacing.roundToPx())
                    .coerceAtLeast(0)

            val leadingPlaceable = measurables
                .fastFirst { it.layoutId == LeadingButtonLayoutId }
                .measure(
                    looseConstraints.copy(
                        minWidth = availableForLeading,
                        maxWidth = availableForLeading,
                        minHeight = constraints.minHeight,
                        maxHeight = constraints.maxHeight
                    )
                )

            val contentWidth =
                leadingPlaceable.width + trailingPlaceable.width + spacing.roundToPx()
            val contentHeight = maxOf(leadingPlaceable.height, trailingPlaceable.height)

            val width = constraints.constrainWidth(contentWidth)
            val height = constraints.constrainHeight(contentHeight)

            layout(width, height) {
                leadingPlaceable.placeRelative(0, 0)
                trailingPlaceable.placeRelative(
                    x = leadingPlaceable.width + spacing.roundToPx(),
                    y = 0
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun SplitButtonPreview() {
    PreviewContainer {
        MediumSplitButton(
            onLeadingClick = {
                // Handle leading button click
            },
            onTrailingClick = {
                // Handle trailing button click
            },
            leadingContent = {
                Icon(
                    modifier = Modifier
                        .size(SplitButtonDefaults.leadingButtonIconSizeFor(SplitButtonDefaults.MediumContainerHeight)),
                    imageVector = AnilibriaIcons.Filled.PlayArrow,
                    contentDescription = null
                )
                Spacer(Modifier.width(ButtonDefaults.MediumIconSpacing))
                Text(
                    text = stringResource(Res.string.button_watch),
                    maxLines = 1
                )
            },
            trailingContent = {
                Icon(
                    modifier = Modifier
                        .size(SplitButtonDefaults.trailingButtonIconSizeFor(SplitButtonDefaults.MediumContainerHeight)),
                    imageVector = AnilibriaIcons.ArrowDropDown,
                    contentDescription = null
                )
            }
        )
    }
}

object SplitButtonDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
        contentColor: Color = contentColorFor(containerColor),
        disabledContainerColor: Color = containerColor.copy(alpha = 0.12f),
        disabledContentColor: Color = contentColor.copy(alpha = 0.38f)
    ): ButtonColors = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    //Temporary fix
    @Composable
    @ExperimentalMaterial3ExpressiveApi
    fun TrailingButton(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        shapes: SplitButtonShapes = SplitButtonDefaults.trailingButtonShapesFor(SmallContainerHeight),
        colors: ButtonColors = ButtonDefaults.buttonColors(),
        elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
        border: BorderStroke? = null,
        contentPadding: PaddingValues = trailingButtonContentPaddingFor(SmallContainerHeight),
        interactionSource: MutableInteractionSource? = null,
        content: @Composable RowScope.() -> Unit,
    ) {
        @Suppress("NAME_SHADOWING")
        val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

        // TODO Load the motionScheme tokens from the component tokens file
        val defaultAnimationSpec = MotionSchemeKeyTokens.DefaultEffects.value<Float>()
        val pressed by interactionSource.collectIsPressedAsState()
        val layoutDirection = LocalLayoutDirection.current
        val density = LocalDensity.current
        val shape = shapeByInteraction(shapes, pressed, checked, defaultAnimationSpec)
        val contentColor = colors.contentColor(enabled)
        val containerColor = colors.containerColor(enabled)

        Surface(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier =
                modifier
                    .drawWithContent {
                        drawContent()
                        if (checked) {
                            drawOutline(
                                outline = shape.createOutline(size, layoutDirection, density),
                                color = contentColor,
                                alpha = TrailingButtonStateLayerAlpha,
                            )
                        }
                    }
                    .semantics { role = Role.Button },
            enabled = enabled,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            shadowElevation = elevation?.shadowElevation(enabled, interactionSource)?.value ?: 0.dp,
            border = border,
            interactionSource = interactionSource,
        ) {
            ProvideContentColorTextStyle(
                contentColor = contentColor,
                textStyle = MaterialTheme.typography.labelLarge,
            ) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = 48.dp,
                            minHeight = SmallContainerHeight,
                        )
                        .then(Modifier)
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun shapeByInteraction(
    shapes: SplitButtonShapes,
    pressed: Boolean,
    checked: Boolean,
    animationSpec: FiniteAnimationSpec<Float>,
): Shape {
    val shape =
        if (pressed) {
            shapes.pressedShape ?: shapes.shape
        } else if (checked) {
            shapes.checkedShape ?: shapes.shape
        } else shapes.shape

    if (shapes.hasRoundedCornerShapes) {
        return rememberAnimatedShape(shape as RoundedCornerShape, animationSpec)
    }
    return shape
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private val SplitButtonShapes.hasRoundedCornerShapes: Boolean
    get() {
        // Ignore null shapes and only check default shape for RoundedCorner
        if (pressedShape != null && pressedShape !is RoundedCornerShape) return false
        if (checkedShape != null && checkedShape !is RoundedCornerShape) return false
        return shape is RoundedCornerShape
    }

private const val LeadingButtonLayoutId = "LeadingButton"
private const val TrailingButtonLayoutId = "TrailingButton"