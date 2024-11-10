package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xbot.anilibriarefresh.ui.components.stickyheaders.StickyHeaders

@Composable
fun <K, V> LazyRowWithStickyHeader(
    items: Map<K, List<V>>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    stickyHeader: @Composable (K) -> Unit,
    itemContent: @Composable LazyItemScope.(V) -> Unit,
) {
    val itemsWithKeys = remember(items) {
        items.flatMap { entry -> entry.value.map { entry.key to it } }
    }

    Column {
        StickyHeaders(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            key = { item ->
                itemsWithKeys.getOrNull(item.index)?.first
            },
            content = {
                stickyHeader(it.key)
            },
        )

        LazyRow(
            modifier = modifier.fillMaxWidth(),
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
        ) {
            items(
                items = itemsWithKeys,
            ) { (_, value) ->
                itemContent(value)
            }
        }
    }
}
