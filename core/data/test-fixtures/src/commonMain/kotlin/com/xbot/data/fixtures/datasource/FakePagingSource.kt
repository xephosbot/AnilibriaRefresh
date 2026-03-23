package com.xbot.data.fixtures.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xbot.domain.models.Release

internal class FakePagingSource(private val data: List<Release>) : PagingSource<Int, Release>() {
    override fun getRefreshKey(state: PagingState<Int, Release>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Release> {
        val page = params.key ?: 0
        val pageSize = params.loadSize
        val start = page * pageSize

        if (start >= data.size) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = if (page > 0) page - 1 else null,
                nextKey = null
            )
        }

        val end = minOf(start + pageSize, data.size)
        val list = data.subList(start, end)

        return LoadResult.Page(
            data = list,
            prevKey = if (page > 0) page - 1 else null,
            nextKey = if (end < data.size) page + 1 else null
        )
    }
}
