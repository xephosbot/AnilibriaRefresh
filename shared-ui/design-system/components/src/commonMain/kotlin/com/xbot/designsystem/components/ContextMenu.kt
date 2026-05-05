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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.utils.LocalIsSinglePane
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContextMenu(
    showMenu: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    menuContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val isDesktop = !LocalIsSinglePane.current

    Box(modifier = modifier) {
        content()

        if (isDesktop) {
            CompositionLocalProvider(
                LocalContextMenuItemOverride provides DefaultContextMenuItemOverride,
            ) {
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = onDismiss
                ) {
                    menuContent()
                }
            }
        }
    }

    if (!isDesktop) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        LaunchedEffect(showMenu) {
            if (showMenu) {
                sheetState.show()
            } else {
                launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (sheetState.isVisible) {
                        onDismiss()
                    }
                }
            }
        }

        if (sheetState.isVisible || showMenu) {
            CompositionLocalProvider(
                LocalContextMenuItemOverride provides MobileContextMenuItemOverride,
            ) {
                ModalBottomSheet(
                    onDismissRequest = onDismiss,
                    sheetState = sheetState,
                ) {
                    menuContent()
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ContextMenuItem(
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    with(LocalContextMenuItemOverride.current) {
        ContextMenuItemOverrideScope(
            label = label,
            modifier = modifier,
            icon = icon,
            onClick = onClick,
        ).ContextMenuItem()
    }
}

interface ContextMenuItemOverride {
    @Composable
    fun ContextMenuItemOverrideScope.ContextMenuItem()
}

@Immutable
class ContextMenuItemOverrideScope(
    val label: String,
    val modifier: Modifier,
    val icon: ImageVector?,
    val onClick: () -> Unit,
)

internal val LocalContextMenuItemOverride = compositionLocalOf<ContextMenuItemOverride> {
    DefaultContextMenuItemOverride
}

internal object DefaultContextMenuItemOverride : ContextMenuItemOverride {
    @Composable
    override fun ContextMenuItemOverrideScope.ContextMenuItem() {
        DropdownMenuItem(
            modifier = modifier,
            text = { Text(label) },
            leadingIcon = icon?.let {
                { Icon(imageVector = it, contentDescription = null) }
            },
            onClick = {
                onClick()
            }
        )
    }
}

internal object MobileContextMenuItemOverride : ContextMenuItemOverride {
    @Composable
    override fun ContextMenuItemOverrideScope.ContextMenuItem() {
        ListItem(
            modifier = modifier.clickable {
                onClick()
            },
            headlineContent = { Text(label) },
            leadingContent = icon?.let {
                { Icon(imageVector = it, contentDescription = null) }
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
    }
}
