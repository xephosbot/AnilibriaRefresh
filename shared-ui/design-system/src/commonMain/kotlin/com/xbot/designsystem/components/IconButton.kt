package com.xbot.designsystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape

@Composable
fun LabeledIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    shape: ExpressiveShape = ExpressiveIconButtonDefaults.smallShape(),
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(
        containerColor = MaterialTheme.colorScheme.surfaceBright,
        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    ),
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

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
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressiveIconButtonDefaults {
    private var _shape: ExpressiveShape? = null

    @Composable
    fun smallShape(): ExpressiveShape {
        return _shape ?: RoundedCornerExpressiveShape(
            shape = IconButtonDefaults.smallRoundShape,
            pressedShape = IconButtonDefaults.smallPressedShape,
            selectedShape = IconButtonDefaults.smallSquareShape,
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
        ).also { _shape = it }
    }

    @Composable
    fun largeShape(): ExpressiveShape {
        return _shape ?: RoundedCornerExpressiveShape(
            shape = IconButtonDefaults.largeRoundShape,
            pressedShape = IconButtonDefaults.largePressedShape,
            selectedShape = IconButtonDefaults.largeSquareShape,
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
        ).also { _shape = it }
    }
}