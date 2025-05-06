package com.xbot.designsystem.components

import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape

@Composable
fun PreferenceLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    verticalSpacing: Dp = 16.dp,
    horizontalPadding: Dp = 16.dp,
    content: PreferenceScope.() -> Unit
) {
    val latestContent = rememberUpdatedState(content)
    val scope by remember(horizontalPadding) {
        derivedStateOf { DefaultPreferenceScope(horizontalPadding).apply(latestContent.value) }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        scope.items.forEach { item ->
            item.itemContent()
        }
    }
}

@Composable
fun PreferenceItem(
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    selected: Boolean = false,
    shape: ExpressiveShape = ExpressivePreferenceItemDefaults.shape(),
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceBright

    Row(
        modifier = modifier
            .clip(shape.shapeForInteraction(pressed, selected))
            .background(color)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 76.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.contentColorFor(color)) {
            icon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )

                Spacer(Modifier.width(20.dp))
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                description?.let { description ->
                    Text(
                        modifier = Modifier.graphicsLayer { alpha = 0.6f },
                        text = description,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }
    }
}

interface PreferenceScope {
    fun item(
        content: @Composable () -> Unit
    )

    fun section(
        content: PreferenceScope.() -> Unit
    )
}

internal class DefaultPreferenceScope(
    private val horizontalPadding: Dp
) : PreferenceScope {
    val items = mutableListOf<PreferenceItem>()

    override fun item(content: @Composable (() -> Unit)) {
        items.add(PreferenceItem(content))
    }

    override fun section(content: PreferenceScope.() -> Unit) {
        val section = @Composable {
            val latestContent = rememberUpdatedState(content)
            val scope by remember(horizontalPadding) {
                derivedStateOf { DefaultPreferenceScope(horizontalPadding).apply(latestContent.value) }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                scope.items.forEachIndexed { index, item ->
                    item.itemContent()
                    if (index != scope.items.lastIndex) {
                        Spacer(Modifier.height(DividerDefaults.Thickness))
                    }
                }
            }
        }
        items.add(PreferenceItem(section))
    }

    internal data class PreferenceItem(
        val itemContent: @Composable () -> Unit
    )
}

fun PreferenceScope.preferenceItem(
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    selected: Boolean = false,
    onClick: () -> Unit
) = item {
    PreferenceItem(
        title = title,
        description = description,
        icon = icon,
        selected = selected,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressivePreferenceItemDefaults {
    private var _shape: ExpressiveShape? = null

    @Composable
    fun shape(): ExpressiveShape {
        return _shape ?: RoundedCornerExpressiveShape(
            shape = RoundedCornerShape(4.dp),
            pressedShape = RoundedCornerShape(24.dp),
            selectedShape = RoundedCornerShape(24.dp),
            animationSpec = spring()
        ).also { _shape = it }
    }
}