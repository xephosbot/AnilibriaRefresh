package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButtonShapes
import androidx.compose.material3.TonalToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.utils.AnilibriaPreviewWrapper

@Composable
fun <T> SingleChoiceConnectedButtonGroup(
    items: List<T>,
    selectedItem: T,
    modifier: Modifier = Modifier,
    spacing: Dp = ConnectedButtonGroupDefaults.Spacing,
    itemContent: @Composable RowScope.(selected: Boolean, item: T) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            itemContent(item == selectedItem, item)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@PreviewWrapper(AnilibriaPreviewWrapper::class)
@Composable
private fun SingleChoiceConnectedButtonGroupPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3")
    var selectedItem by remember { mutableStateOf(items[0]) }

    SingleChoiceConnectedButtonGroup(
        items = items,
        selectedItem = selectedItem,
    ) { selected, item ->
        TonalToggleButton(
            checked = selected,
            onCheckedChange = { selectedItem = item },
            shapes = ConnectedButtonGroupDefaults.connectedButtonShapes(
                index = items.indexOf(item),
                count = items.size
            ),
            contentPadding = ButtonDefaults.ExtraSmallContentPadding,
        ) {
            Text(text = item)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ConnectedButtonGroupDefaults {
    val Spacing = 2.dp

    @Composable
    fun connectedButtonShapes(
        index: Int,
        count: Int,
    ): ToggleButtonShapes {
        return when (index) {
            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
            count - 1 -> ButtonGroupDefaults.connectedTrailingButtonShapes()
            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
        }
    }
}