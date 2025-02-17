package com.xbot.designsystem.components

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.xbot.domain.models.Release

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
    content: FeedScope.() -> Unit
) {
    val latestContent = rememberUpdatedState(content)
    val feedScope by remember { derivedStateOf { DefaultFeedScope().apply(latestContent.value) } }
    val feedItemScope = remember { DefaultFeedItemScope() }

    LazyVerticalGrid(
        columns = columns,
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled
    ) {
        feedScope.items.forEach { feedItem ->
            items(
                count = feedItem.count,
                key = feedItem.key,
                contentType = feedItem.contentType,
                span = if (feedItem.span != null) {
                    {
                        feedItem.span!!(this, it).also { itemSpan ->
                            feedItemScope._currentLineSpan = itemSpan.currentLineSpan
                        }
                    }
                } else {
                    null
                },
                itemContent = {
                    feedItem.itemContent(feedItemScope, it)
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
        content: @Composable FeedItemScope.() -> Unit
    )

    fun items(
        count: Int,
        key: ((index: Int) -> Any)? = null,
        span: (LazyGridItemSpanScope.(index: Int) -> GridItemSpan)? = null,
        contentType: (index: Int) -> Any? = { null },
        itemContent: @Composable FeedItemScope.(index: Int) -> Unit
    )
}

interface FeedItemScope {
    val currentLineSpan: Int

    fun Modifier.feedItemSpacing(
        index: Int,
        horizontalSpacing: Dp = 16.dp
    ): Modifier {
        val halfSpacing = horizontalSpacing / 2

        try {
            val isFirstColumn = index % currentLineSpan == 0
            val isLastColumn = (index + 1) % currentLineSpan == 0

            return padding(
                start = if (isFirstColumn) horizontalSpacing else halfSpacing,
                end = if (isLastColumn) horizontalSpacing else halfSpacing
            )
        } catch (e: Exception) {
            return padding(
                start = horizontalSpacing,
                end = horizontalSpacing
            )
        }
    }
}

internal class DefaultFeedScope : FeedScope {
    val items = mutableListOf<FeedItem>()

    override fun item(
        key: Any?,
        span: (LazyGridItemSpanScope.() -> GridItemSpan)?,
        contentType: Any?,
        content: @Composable FeedItemScope.() -> Unit
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
        itemContent: @Composable FeedItemScope.(index: Int) -> Unit
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
        val itemContent: @Composable FeedItemScope.(index: Int) -> Unit
    )
}

internal class DefaultFeedItemScope : FeedItemScope {
    internal var _currentLineSpan: Int = 0

    override val currentLineSpan: Int
        get() = _currentLineSpan
}

inline fun <T> FeedScope.items(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable FeedItemScope.(item: T) -> Unit
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
    crossinline itemContent: @Composable FeedItemScope.(index: Int, item: T) -> Unit
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
    crossinline itemContent: @Composable FeedItemScope.(item: T) -> Unit
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
    crossinline itemContent: @Composable FeedItemScope.(index: Int, item: T) -> Unit
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
    crossinline content: @Composable FeedItemScope.() -> Unit
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
    crossinline content: @Composable FeedItemScope.() -> Unit
) = row(key = key, contentType = contentType) { content() }

inline fun <T> FeedScope.horizontalItems(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    itemSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(),
    crossinline itemContent: @Composable LazyItemScope.(T) -> Unit,
) = row {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        contentPadding = contentPadding,
    ) {
        items(
            items = items,
            key = key,
            contentType = contentType
        ) {
            itemContent(it)
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
    snapPosition: SnapPosition = SnapPosition.Start,
    crossinline pagerContent: @Composable PagerScope.(index: Int, item: T) -> Unit,
) = row {
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
    crossinline itemContent: @Composable FeedItemScope.(index: Int, item: T?) -> Unit,
) {
    items(
        count = items.itemCount,
        key = key,
        contentType = items.itemContentType { "Paging Items" },
    ) {
        itemContent(it, items[it])
    }
}
