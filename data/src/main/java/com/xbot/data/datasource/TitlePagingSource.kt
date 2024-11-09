package com.xbot.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xbot.data.BuildConfig
import com.xbot.data.datasource.TitleDataSource.Companion.NETWORK_PAGE_SIZE
import com.xbot.domain.models.TitleModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.max

class TitlePagingSource @Inject constructor(
    private val dataSource: TitleDataSource
) : PagingSource<Int, TitleModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TitleModel> {
        val pageIndex = params.key ?: FIRST_PAGE_INDEX
        val loadSize = params.loadSize
        return try {
            val page = dataSource.getLatestTitles(pageIndex + 1, loadSize).first()

            val newCount = page.items.size
            val total = page.total
            val itemsBefore = pageIndex * NETWORK_PAGE_SIZE
            val itemsAfter = max(total - (itemsBefore + newCount), 0)

            val prevKey = if (pageIndex == FIRST_PAGE_INDEX) null else pageIndex - 1
            val nextKey = if (itemsAfter == 0) null else pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)

            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Current loaded page index: $pageIndex")
            }

            LoadResult.Page(
                data = page.items,
                prevKey = prevKey,
                nextKey = nextKey,
                itemsBefore = itemsBefore,
                itemsAfter = itemsAfter
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TitleModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val pageIndex = anchorPosition / NETWORK_PAGE_SIZE
        return pageIndex
    }

    override val jumpingSupported: Boolean = true

    companion object {
        private const val FIRST_PAGE_INDEX = 0
        private const val TAG = "TitlePagingSource"
    }
}