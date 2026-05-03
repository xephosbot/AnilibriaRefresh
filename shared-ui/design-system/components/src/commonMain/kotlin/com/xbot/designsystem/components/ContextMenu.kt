package com.xbot.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.utils.LocalIsSinglePane

@Composable
fun ContextMenu(
    showMenu: Boolean,
    onDismiss: () -> Unit,
    items: List<ContextMenuItem>,
    content: @Composable () -> Unit,
) {
    val isDesktop = !LocalIsSinglePane.current

    Box {
        content()

        if (isDesktop) {
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = onDismiss
            ) {
                items.forEach { item ->
                    ContextMenuDropdownItem(
                        item = item,
                        onDismiss = onDismiss
                    )
                }
            }
        }
    }

    if (!isDesktop && showMenu) {
        ContextMenuBottomSheet(
            items = items,
            onDismiss = onDismiss
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContextMenuBottomSheet(
    items: List<ContextMenuItem>,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        items.forEach { item ->
            ContextMenuListItem(
                item = item,
                onDismiss = onDismiss
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ContextMenuListItem(
    item: ContextMenuItem,
    onDismiss: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable {
            item.onClick()
            onDismiss()
        },
        headlineContent = { Text(item.label) },
        leadingContent = item.icon?.let {
            { Icon(imageVector = it, contentDescription = null) }
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
private fun ContextMenuDropdownItem(
    item: ContextMenuItem,
    onDismiss: () -> Unit,
) {
    DropdownMenuItem(
        text = { Text(item.label) },
        leadingIcon = item.icon?.let {
            { Icon(imageVector = it, contentDescription = null) }
        },
        onClick = {
            item.onClick()
            onDismiss()
        }
    )
}

data class ContextMenuItem(
    val icon: ImageVector? = null,
    val label: String,
    val onClick: () -> Unit,
)