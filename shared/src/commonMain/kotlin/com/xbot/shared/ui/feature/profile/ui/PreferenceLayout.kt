package com.xbot.shared.ui.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun PreferenceLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    verticalSpacing: Dp = 16.dp,
    horizontalPadding: Dp = 16.dp,
    content: PreferenceScope.() -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        val scope = remember { DefaultPreferenceScope(horizontalPadding).apply(content) }
        scope.items.forEach { item ->
            item.itemContent()
        }
    }
}

@Composable
internal fun PreferenceItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceBright)
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }

        Spacer(Modifier.width(20.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
            Text(
                modifier = Modifier.graphicsLayer { alpha = 0.6f },
                text = description,
                style = MaterialTheme.typography.titleSmall,
            )
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
            Column(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                val scope = remember { DefaultPreferenceScope(horizontalPadding).apply(content) }
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
    description: String,
    icon: ImageVector? = null,
    onClick: () -> Unit
) = item {
    PreferenceItem(
        title = title,
        description = description,
        icon = icon,
        onClick = onClick
    )
}