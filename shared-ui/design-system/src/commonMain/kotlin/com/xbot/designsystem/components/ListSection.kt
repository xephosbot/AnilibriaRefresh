package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@DslMarker annotation class ListSectionScopeMaker

@ListSectionScopeMaker
interface ListSectionScope {
    fun items(
        count: Int,
        key: ((index: Int) -> Any)? = null,
        contentType: (index: Int) -> Any? = { null },
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit
    )

    fun item(
        key: Any? = null,
        contentType: Any? = null,
        content: @Composable LazyItemScope.() -> Unit
    )
}

inline fun <T> ListSectionScope.items(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) =
    items(
        count = items.size,
        key = if (key != null) { index: Int -> key(items[index]) } else null,
        contentType = { index: Int -> contentType(items[index]) }
    ) {
        itemContent(items[it])
    }

inline fun <T> ListSectionScope.itemsIndexed(
    items: List<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) =
    items(
        count = items.size,
        key = if (key != null) { index: Int -> key(index, items[index]) } else null,
        contentType = { index -> contentType(index, items[index]) }
    ) {
        itemContent(it, items[it])
    }

fun LazyListScope.section(
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
    content: ListSectionScope.() -> Unit
) {
    header?.let {
        item {
            header()
        }
    }

    val scope = DefaultListSectionItemProvider().apply(content)
    scope.items.forEach { sectionItem ->
        items(
            count = sectionItem.count,
            key = sectionItem.key,
            contentType = sectionItem.contentType,
            itemContent = { index ->
                val globalIndex = index + sectionItem.offset
                Box(
                    modifier = Modifier
                        .padding(
                            horizontal = ListSectionDefaults.ListItemHorizontalPadding
                        )
                        .clip(
                            shape = ListSectionDefaults.listItemShapes(
                                index = globalIndex,
                                count = scope.itemsCount
                            )
                        )
                ) {
                    sectionItem.content(this@items, index)
                }
                if (globalIndex < scope.itemsCount - 1) {
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        )
    }

    footer?.let {
        item {
            footer()
        }
    }
}

object ListSectionDefaults {

    private val InnerCornerRadius = 4.dp
    internal val ListItemHorizontalPadding = 16.dp

    @Composable
    private fun leadingListItemShapes(): Shape = MaterialTheme.shapes.large.copy(
        bottomStart = CornerSize(InnerCornerRadius),
        bottomEnd = CornerSize(InnerCornerRadius),
    )

    @Composable
    private fun trailingListItemShapes(): Shape = MaterialTheme.shapes.large.copy(
        topStart = CornerSize(InnerCornerRadius),
        topEnd = CornerSize(InnerCornerRadius),
    )

    @Composable
    private fun middleListItemShapes(): Shape = RoundedCornerShape(InnerCornerRadius)

    @Composable
    fun listItemShapes(
        index: Int,
        count: Int
    ): Shape {
        if (count == 1) return MaterialTheme.shapes.large
        return when (index) {
            0 -> leadingListItemShapes()
            count - 1 -> trailingListItemShapes()
            else -> middleListItemShapes()
        }
    }
}

internal interface ListSectionItemProvider {
    val items: MutableList<ListSectionItem>
    val itemsCount: Int
}

internal class DefaultListSectionItemProvider : ListSectionScope, ListSectionItemProvider {
    override fun items(
        count: Int,
        key: ((index: Int) -> Any)?,
        contentType: (index: Int) -> Any?,
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit
    ) {
        items.add(
            ListSectionItem(
                count = count,
                offset = itemsCount,
                key = key,
                contentType = contentType,
                content = itemContent
            )
        )
    }

    override fun item(
        key: Any?,
        contentType: Any?,
        content: @Composable LazyItemScope.() -> Unit
    ) {
        items.add(
            ListSectionItem(
                count = 1,
                offset = itemsCount,
                key = if (key != null) { { key } } else { null },
                contentType = { contentType },
                content = { content() }
            )
        )
    }

    override val items: MutableList<ListSectionItem> = mutableListOf()

    override val itemsCount: Int get() = items.sumOf { it.count }
}

internal data class ListSectionItem(
    val count: Int,
    val offset: Int,
    val key: ((index: Int) -> Any)?,
    val contentType: (index: Int) -> Any?,
    val content: @Composable LazyItemScope.(index: Int) -> Unit
)