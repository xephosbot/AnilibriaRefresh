package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

fun <T> LazyGridScope.horizontalItems(
    items: List<T>,
    key: ((item: T) -> Any)? = null,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    contentPadding: PaddingValues = PaddingValues(),
    itemContent: @Composable LazyItemScope.(T) -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { ContentType.HorizontalList },
    ) {
        LazyRow(
            horizontalArrangement = horizontalArrangement,
            contentPadding = contentPadding,
        ) {
            items(
                items = items,
                key = key,
            ) {
                itemContent(it)
            }
        }
    }
}

fun <K, V> LazyGridScope.horizontalItems(
    items: Map<K, List<V>>,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    contentPadding: PaddingValues = PaddingValues(),
    stickyHeader: @Composable (K) -> Unit,
    itemContent: @Composable LazyItemScope.(V) -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { ContentType.HorizontalList },
    ) {
        LazyRowWithStickyHeader(
            items = items,
            contentPadding = contentPadding,
            horizontalArrangement = horizontalArrangement,
            stickyHeader = stickyHeader,
            itemContent = itemContent,
        )
    }
}

fun LazyGridScope.horizontalPagerItems(
    state: PagerState,
    pagerContent: @Composable PagerScope.(index: Int) -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { ContentType.CarouselItems },
    ) {
        HorizontalPager(
            state = state
        ) { page ->
            pagerContent(page)
        }
    }
}

private enum class ContentType {
    HorizontalList,
    CarouselItems,
    PagingItems
}