package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.Check
import com.xbot.designsystem.modifier.animatePlacement
import com.xbot.designsystem.theme.AnilibriaTheme

@Composable
fun <T> MultiChoiceChipGroup(
    items: List<T>,
    selectedItems: List<T>,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    contentPadding: PaddingValues = ChipGroupDefaults.ContentPadding,
    spacing: Dp = ChipGroupDefaults.ChipSpacing,
    itemContent: @Composable ChipGroupScope.(selected: Boolean, item: T) -> Unit
) {
    ChipGroup(
        items = items,
        modifier = modifier,
        maxLines = maxLines,
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
    maxLines: Int = Int.MAX_VALUE,
    contentPadding: PaddingValues = ChipGroupDefaults.ContentPadding,
    spacing: Dp = ChipGroupDefaults.ChipSpacing,
    itemContent: @Composable ChipGroupScope.(selected: Boolean, item: T) -> Unit
) {
    ChipGroup(
        items = items,
        modifier = modifier,
        maxLines = maxLines,
        contentPadding = contentPadding,
        spacing = spacing
    ) { item ->
        itemContent(item == selectedItem, item)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> ChipGroup(
    items: List<T>,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    contentPadding: PaddingValues = ChipGroupDefaults.ContentPadding,
    spacing: Dp = ChipGroupDefaults.ChipSpacing,
    itemContent: @Composable ChipGroupScope.(item: T) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        maxLines = maxLines,
        modifier = modifier
            .wrapContentHeight()
            .padding(contentPadding)
    ) {
        val scope = remember { DefaultChipGroupScope(this) }
        items.forEach { item ->
            scope.itemContent(item)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipGroup(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    contentPadding: PaddingValues = ChipGroupDefaults.ContentPadding,
    spacing: Dp = ChipGroupDefaults.ChipSpacing,
    content: @Composable ChipGroupScope.() -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        maxLines = maxLines,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(contentPadding)
    ) {
        val scope = remember { DefaultChipGroupScope(this) }
        scope.content()
    }
}


@Preview
@Composable
private fun MultiChoiceChipGroupPreview() {
    val items = remember { listOf("summer", "autumn", "winter", "spring") }
    val selectedItems = remember { mutableStateListOf("summer", "winter") }

    AnilibriaTheme {
        Surface {
            MultiChoiceChipGroup(
                items = items,
                selectedItems = selectedItems
            ) { selected, item ->
                FilterChip(
                    modifier = Modifier.animatePlacement(),
                    selected = selected,
                    onClick = {
                        if (selectedItems.contains(item)) {
                            selectedItems.remove(item)
                        } else {
                            selectedItems.add(item)
                        }
                    },
                    label = { Text(item) },
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                AnilibriaIcons.Check,
                                null,
                                Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
            }
        }
    }
}

@Preview
@Composable
private fun SingleChoiceChipGroupPreview() {
    val items = remember { listOf("summer", "autumn", "winter", "spring") }
    var selectedItem by remember { mutableStateOf("summer") }

    AnilibriaTheme {
        Surface {
            SingleChoiceChipGroup(
                items = items,
                selectedItem = selectedItem
            ) { selected, item ->
                FilterChip(
                    modifier = Modifier.animatePlacement(),
                    selected = selected,
                    onClick = {
                        selectedItem = item
                    },
                    label = { Text(item) },
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                AnilibriaIcons.Check,
                                null,
                                Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
interface ChipGroupScope : FlowRowScope

@OptIn(ExperimentalLayoutApi::class)
internal class DefaultChipGroupScope(scope: FlowRowScope) : ChipGroupScope, FlowRowScope by scope

object ChipGroupDefaults {
    val ChipSpacing = 8.dp
    val ContentPadding = PaddingValues(horizontal = 16.dp)
}