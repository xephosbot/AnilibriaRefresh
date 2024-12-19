package com.xbot.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xbot.domain.models.utils.PagedResponse
import kotlin.math.max

open class CommonPagingSource<T : Any>(
    private val loadPage: suspend (page: Int, limit: Int) -> PagedResponse<T>,
) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageIndex = params.key ?: FIRST_PAGE_INDEX
        val loadSize = params.loadSize
        return try {
            val page = loadPage(pageIndex + 1, loadSize)

            val newCount = page.items.size
            val total = page.total
            val itemsBefore = pageIndex * NETWORK_PAGE_SIZE
            val itemsAfter = max(total - (itemsBefore + newCount), 0)

            val prevKey = if (pageIndex == FIRST_PAGE_INDEX) null else pageIndex - 1
            val nextKey = if (itemsAfter == 0) null else pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)

            LoadResult.Page(
                data = page.items,
                prevKey = prevKey,
                nextKey = nextKey,
                itemsBefore = itemsBefore,
                itemsAfter = itemsAfter,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val pageIndex = anchorPosition / NETWORK_PAGE_SIZE
        return pageIndex
    }

    override val jumpingSupported: Boolean = true

    companion object {
        const val NETWORK_PAGE_SIZE = 20
        private const val FIRST_PAGE_INDEX = 0
    }
}
