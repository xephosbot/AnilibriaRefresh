package com.xbot.designsystem.components

import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.ExpressiveTextStyle
import com.xbot.designsystem.theme.MorphingExpressiveTextStyle
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: ExpressiveShape = ExpressiveButtonDefaults.shape(),
    textStyle: ExpressiveTextStyle = ExpressiveButtonDefaults.textStyle(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val buttonShape = shape.shapeForInteraction(pressed, false)
    val buttonTextStyle = textStyle.textStyleForInteraction(pressed)

    Button(
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
        shape = buttonShape,
        colors = colors,
        contentPadding = contentPadding,
    ) {
        ProvideTextStyle(buttonTextStyle.copy(textMotion = TextMotion.Animated)) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressiveButtonDefaults {
    private var _shape: ExpressiveShape? = null
    private var _textStyle: ExpressiveTextStyle? = null

    @Composable
    fun shape(): ExpressiveShape {
        return _shape ?: RoundedCornerExpressiveShape(
            shape = ButtonDefaults.shape,
            pressedShape = ButtonDefaults.pressedShape,
            selectedShape = ButtonDefaults.squareShape,
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
        ).also { _shape = it }
    }

    @Composable
    fun textStyle(): ExpressiveTextStyle {
        return _textStyle ?: MorphingExpressiveTextStyle(
            from = MaterialTheme.typography.labelLarge,
            to = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
            animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
        ).also { _textStyle = it }
    }
}