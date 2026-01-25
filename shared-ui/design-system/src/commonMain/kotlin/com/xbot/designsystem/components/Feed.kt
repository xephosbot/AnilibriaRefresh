package com.xbot.designsystem.components

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

@Composable
fun Feed(
    columns: GridCells,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    content: FeedScope.() -> Unit
) {
    val latestContent = rememberUpdatedState(content)
    val feedScope by remember {
        derivedStateOf { DefaultFeedScope().apply(latestContent.value) }
    }

    LazyVerticalGrid(
        columns = columns,
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        overscrollEffect = overscrollEffect,
    ) {
        feedScope.items.forEach { feedItem ->
            items(
                count = feedItem.count,
                key = feedItem.key,
                contentType = feedItem.contentType,
                span = if (feedItem.span != null) {
                    { index ->
                        feedItem.span.invoke(this, index)
                    }
                } else null,
                itemContent = { index ->
                    feedItem.itemContent(this, index)
                }
            )
        }
    }
}

interface FeedScope {
    fun item(
        key: Any? = null,
        span: (LazyGridItemSpanScope.() -> GridItemSpan)? = null,
        contentType: Any? = null,
        content: @Composable LazyGridItemScope.() -> Unit
    )

    fun items(
        count: Int,
        key: ((index: Int) -> Any)? = null,
        span: (LazyGridItemSpanScope.(index: Int) -> GridItemSpan)? = null,
        contentType: (index: Int) -> Any? = { null },
        itemContent: @Composable LazyGridItemScope.(index: Int) -> Unit
    )
}

internal class DefaultFeedScope : FeedScope {
    val items = mutableListOf<FeedItem>()

    override fun item(
        key: Any?,
        span: (LazyGridItemSpanScope.() -> GridItemSpan)?,
        contentType: Any?,
        content: @Composable LazyGridItemScope.() -> Unit
    ) {
        items.add(
            FeedItem(
                count = 1,
                key = if (key != null) {
                    { key }
                } else {
                    null
                },
                span = if (span != null) {
                    { span() }
                } else {
                    null
                },
                contentType = { contentType },
                itemContent = { content() }
            )
        )
    }

    override fun items(
        count: Int,
        key: ((index: Int) -> Any)?,
        span: (LazyGridItemSpanScope.(index: Int) -> GridItemSpan)?,
        contentType: (index: Int) -> Any?,
        itemContent: @Composable LazyGridItemScope.(index: Int) -> Unit
    ) {
        items.add(
            FeedItem(
                count = count,
                key = key,
                span = span,
                contentType = contentType,
                itemContent = itemContent
            )
        )
    }

    internal data class FeedItem(
        val count: Int,
        val key: ((index: Int) -> Any)?,
        val span: (LazyGridItemSpanScope.(index: Int) -> GridItemSpan)?,
        val contentType: (index: Int) -> Any?,
        val itemContent: @Composable LazyGridItemScope.(index: Int) -> Unit
    )
}

inline fun <T> FeedScope.items(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyGridItemScope.(item: T) -> Unit
) = items(
    count = items.size,
    key = if (key != null) {
        { index: Int -> key(items[index]) }
    } else {
        null
    },
    span = if (span != null) {
        { index: Int -> span(items[index]) }
    } else {
        null
    },
    contentType = { index: Int -> contentType(items[index]) }
) { index ->
    itemContent(items[index])
}

inline fun <T> FeedScope.itemsIndexed(
    items: List<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(index: Int, item: T) -> GridItemSpan)? = null,
    noinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyGridItemScope.(index: Int, item: T) -> Unit
) = items(
    count = items.size,
    key = if (key != null) {
        { index: Int -> key(index, items[index]) }
    } else {
        null
    },
    span = if (span != null) {
        { index: Int -> span(index, items[index]) }
    } else {
        null
    },
    contentType = { index: Int -> contentType(index, items[index]) }
) { index ->
    itemContent(index, items[index])
}

inline fun <T> FeedScope.items(
    items: Array<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyGridItemScope.(item: T) -> Unit
) = items(
    count = items.size,
    key = if (key != null) {
        { index: Int -> key(items[index]) }
    } else {
        null
    },
    span = if (span != null) {
        { index: Int -> span(items[index]) }
    } else {
        null
    },
    contentType = { index: Int -> contentType(items[index]) }
) { index ->
    itemContent(items[index])
}

inline fun <T> FeedScope.itemsIndexed(
    items: Array<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(index: Int, item: T) -> GridItemSpan)? = null,
    noinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyGridItemScope.(index: Int, item: T) -> Unit
) = items(
    count = items.size,
    key = if (key != null) {
        { index: Int -> key(index, items[index]) }
    } else {
        null
    },
    span = if (span != null) {
        { index: Int -> span(index, items[index]) }
    } else {
        null
    },
    contentType = { index: Int -> contentType(index, items[index]) }
) { index ->
    itemContent(index, items[index])
}

inline fun FeedScope.row(
    key: Any? = null,
    contentType: Any? = null,
    crossinline content: @Composable LazyGridItemScope.() -> Unit
) = item(
    key = key,
    span = { GridItemSpan(maxLineSpan) },
    contentType = contentType
) {
    content()
}

inline fun FeedScope.title(
    key: Any? = null,
    contentType: Any? = null,
    crossinline content: @Composable LazyGridItemScope.() -> Unit
) = row(key = key, contentType = contentType) { content() }

inline fun <T> FeedScope.horizontalItems(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    itemSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(),
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit,
) = row {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        contentPadding = contentPadding,
    ) {
        items(
            items = items,
            key = key,
            contentType = contentType
        ) { item ->
            itemContent(item)
        }
    }
}

inline fun <T> FeedScope.horizontalSnappableItems(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    itemSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(),
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit,
) = row {
    val state = rememberLazyListState()
    val snappingLayout = remember(state) { SnapLayoutInfoProvider(state, SnapPosition.Start) }
    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)

    LazyRow(
        state = state,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        contentPadding = contentPadding,
        flingBehavior = flingBehavior,
    ) {
        items(
            items = items,
            key = key,
            contentType = contentType
        ) { item ->
            itemContent(item)
        }
    }
}

inline fun <T> FeedScope.horizontalItemsIndexed(
    items: List<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    noinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    itemSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(),
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
) = row {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        contentPadding = contentPadding,
    ) {
        itemsIndexed(
            items = items,
            key = key,
            contentType = contentType
        ) { index, item ->
            itemContent(index, item)
        }
    }
}

inline fun <K, V> FeedScope.horizontalItems(
    items: Map<K, List<V>>,
    itemSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(),
    crossinline stickyHeader: @Composable (K) -> Unit,
    crossinline itemContent: @Composable LazyItemScope.(V) -> Unit,
) = row {
    LazyRowWithStickyHeader(
        items = items,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        stickyHeader = { stickyHeader(it) },
        itemContent = { itemContent(it) },
    )
}

inline fun <T> FeedScope.horizontalPagerItems(
    items: List<T>,
    state: PagerState,
    noinline key: ((index: Int) -> Any)? = null,
    pageSize: PageSize = PageSize.Fill,
    beyondViewportPageCount: Int = PagerDefaults.BeyondViewportPageCount,
    pageSpacing: Dp = 0.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    userScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    isAutoScrollActive: Boolean = true,
    snapPosition: SnapPosition = SnapPosition.Start,
    crossinline pagerContent: @Composable PagerScope.(index: Int, item: T) -> Unit,
) = row {
    val performAutoScroll = shouldPerformAutoScroll(state.interactionSource)

    AutoScrollSideEffect(
        autoScrollDurationMillis = 5000L,
        pagerState = state,
        doAutoScroll = performAutoScroll && isAutoScrollActive
    )

    HorizontalPager(
        state = state,
        key = key,
        pageSize = pageSize,
        beyondViewportPageCount = beyondViewportPageCount,
        pageSpacing = pageSpacing,
        verticalAlignment = verticalAlignment,
        userScrollEnabled = userScrollEnabled,
        reverseLayout = reverseLayout,
        snapPosition = snapPosition
    ) { page ->
        pagerContent(page, items[page])
    }
}

inline fun FeedScope.header(
    crossinline title: @Composable () -> Unit,
    noinline onClick: (() -> Unit)? = null
) = row {
    Header(
        title = { title() },
        onClick = onClick
    )
}

inline fun <T : Any> FeedScope.pagingItems(
    items: LazyPagingItems<T>,
    noinline key: ((index: Int) -> Any)? = items.itemKey(),
    crossinline itemContent: @Composable LazyGridItemScope.(index: Int, item: T?) -> Unit,
) {
    items(
        count = items.itemCount,
        key = key,
        contentType = items.itemContentType { "Paging Items" },
    ) {
        itemContent(it, items[it])
    }
}