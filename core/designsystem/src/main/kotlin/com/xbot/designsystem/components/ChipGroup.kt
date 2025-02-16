package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> MultiChoiceChipGroup(
    items: List<T>,
    selectedItems: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ChipGroupDefaults.ContentPadding,
    spacing: Dp = ChipGroupDefaults.ChipSpacing,
    itemContent: @Composable ChipGroupScope.(selected: Boolean, item: T) -> Unit
) {
    ChipGroup(
        items = items,
        modifier = modifier,
        contentPadding = contentPadding,
        spacing = spacing
    ) { item ->
        itemContent(selectedItems.contains(item), item)
    }
}

@Composable
fun <T> SingleChoiceChipGroup(
    items: List<T>,
    selectedItem: T,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ChipGroupDefaults.ContentPadding,
    spacing: Dp = ChipGroupDefaults.ChipSpacing,
    itemContent: @Composable ChipGroupScope.(selected: Boolean, item: T) -> Unit
) {
    ChipGroup(
        items = items,
        modifier = modifier,
        contentPadding = contentPadding,
        spacing = spacing
    ) { item ->
        itemContent(item == selectedItem, item)
    }
}

@Composable
fun <T> ChipGroup(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ChipGroupDefaults.ContentPadding,
    spacing: Dp = ChipGroupDefaults.ChipSpacing,
    itemContent: @Composable ChipGroupScope.(item: T) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(contentPadding)
    ) {
        val scope = remember { DefaultChipGroupScope(this) }
        items.forEach { item ->
            scope.itemContent(item)
        }
    }
}

@Composable
fun ChipGroup(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ChipGroupDefaults.ContentPadding,
    spacing: Dp = ChipGroupDefaults.ChipSpacing,
    content: @Composable ChipGroupScope.() -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(contentPadding)
    ) {
        val scope = remember { DefaultChipGroupScope(this) }
        scope.content()
    }
}

interface ChipGroupScope : FlowRowScope

internal class DefaultChipGroupScope(scope: FlowRowScope): ChipGroupScope, FlowRowScope by scope

object ChipGroupDefaults {
    val ChipSpacing = 8.dp
    val ContentPadding = PaddingValues(horizontal = 16.dp)
}