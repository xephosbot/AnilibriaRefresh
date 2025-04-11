package com.xbot.shared.ui.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun PreferenceLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    content: PreferenceScope.() -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
    ) {
        val scope = remember { DefaultPreferenceScope().apply(content) }
        scope.items.forEach { item ->
            item.itemContent(PaddingValues())
        }
    }
}

@Composable
internal fun PreferenceItem(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }

        Spacer(Modifier.width(20.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
        )
    }
}

interface PreferenceScope {
    fun item(
        content: @Composable (PaddingValues) -> Unit
    )

    fun section(
        padding: PaddingValues,
        content: PreferenceScope.() -> Unit
    )
}

internal class DefaultPreferenceScope : PreferenceScope {
    val items = mutableListOf<PreferenceItem>()

    override fun item(content: @Composable ((PaddingValues) -> Unit)) {
        items.add(PreferenceItem(content))
    }

    override fun section(
        padding: PaddingValues,
        content: PreferenceScope.() -> Unit
    ) {
        items.add(
            PreferenceItem(
                itemContent = {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surfaceBright)
                    ) {
                        val scope = remember { DefaultPreferenceScope().apply(content) }
                        scope.items.forEach { item ->
                            item.itemContent(padding)
                        }
                    }
                }
            )
        )
    }

    internal data class PreferenceItem(
        val itemContent: @Composable (PaddingValues) -> Unit
    )
}

fun PreferenceScope.preferenceItem(
    title: String,
    icon: ImageVector? = null,
    onClick: () -> Unit
) = item { contentPadding ->
    PreferenceItem(
        title = title,
        icon = icon,
        contentPadding = contentPadding,
        onClick = onClick
    )
}