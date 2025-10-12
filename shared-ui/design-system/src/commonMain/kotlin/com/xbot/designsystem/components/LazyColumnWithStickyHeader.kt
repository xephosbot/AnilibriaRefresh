package com.xbot.designsystem.components

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.gingerninja.lazy.StickyHeaders

@Composable
fun <K, V> LazyColumnWithStickyHeader(
    items: Map<K, List<V>>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    stickyEdgePadding: Dp = contentPadding.calculateTopPadding(),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    stickyHeader: @Composable (K) -> Unit,
    itemContent: @Composable LazyItemScope.(V) -> Unit,
) {
    val itemsWithKeys = remember(items) {
        items.flatMap { entry -> entry.value.map { entry.key to it } }
    }

    Row {
        StickyHeaders(
            modifier = Modifier.fillMaxHeight(),
            stickyEdgePadding = stickyEdgePadding,
            state = state,
            key = { item ->
                itemsWithKeys.getOrNull(item.index)?.first
            },
            content = {
                stickyHeader(it.key)
            },
        )
        LazyColumn(
            modifier = modifier.fillMaxHeight(),
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            overscrollEffect = overscrollEffect,
        ) {
            itemsIndexed(
                items = itemsWithKeys,
            ) { index, (_, value) ->
                itemContent(value)
            }
        }
    }
}