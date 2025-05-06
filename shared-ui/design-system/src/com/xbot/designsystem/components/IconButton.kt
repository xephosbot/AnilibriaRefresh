package com.xbot.designsystem.components

import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.ExpressiveTextStyle
import com.xbot.designsystem.theme.MorphingExpressiveTextStyle
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilledIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ),
    shape: ExpressiveShape = ExpressiveIconButtonDefaults.shape(),
    iconButtonWidthOption: IconButtonDefaults.IconButtonWidthOption = IconButtonDefaults.IconButtonWidthOption.Uniform,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Surface(
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        shape = shape.shapeForInteraction(pressed, false),
        color = colors.containerColor,
        contentColor = colors.contentColor,
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier.size(IconButtonDefaults.smallContainerSize(iconButtonWidthOption)),
            contentAlignment = Alignment.Center
        ) {
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
    shape: ExpressiveShape = ExpressiveIconButtonDefaults.shape(),
    textStyle: ExpressiveTextStyle = ExpressiveIconButtonDefaults.textStyle(),
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(
        containerColor = MaterialTheme.colorScheme.surfaceBright,
        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    ),
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val buttonTextStyle = textStyle.textStyleForInteraction(pressed)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledIconButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            colors = colors,
            shape = shape.shapeForInteraction(pressed, false),
            interactionSource = interactionSource
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
        CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
            Text(
                text = text,
                style = buttonTextStyle.copy(textMotion = TextMotion.Animated),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressiveIconButtonDefaults {
    private var _shape: ExpressiveShape? = null
    private var _textStyle: ExpressiveTextStyle? = null

    @Composable
    fun shape(): ExpressiveShape {
        return _shape ?: RoundedCornerExpressiveShape(
            shape = IconButtonDefaults.standardShape,
            pressedShape = IconButtonDefaults.smallPressedShape,
            selectedShape = IconButtonDefaults.smallPressedShape,
            animationSpec = spring()
        ).also { _shape = it }
    }

    @Composable
    fun textStyle(): ExpressiveTextStyle {
        return _textStyle ?: MorphingExpressiveTextStyle(
            from = MaterialTheme.typography.labelMedium,
            to = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
            animationSpec = spring()
        ).also { _textStyle = it }
    }
}