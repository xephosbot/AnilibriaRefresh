package com.xbot.designsystem.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalScrollableBottomSheet(
    onDismissRequest: () -> Unit,
    scrollableState: ScrollableState,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollAtTop by remember {
        derivedStateOf {
            when (scrollableState) {
                is LazyListState ->
                    scrollableState.firstVisibleItemIndex == 0 && scrollableState.firstVisibleItemScrollOffset == 0
                is ScrollState -> scrollableState.value == 0
                else -> throw IllegalArgumentException()
            }

        }
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetGesturesEnabled = scrollAtTop,
        content = content
    )
}