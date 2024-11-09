package com.xbot.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xbot.data.datasource.TitleDataSource
import com.xbot.data.datasource.TitleDataSource.Companion.NETWORK_PAGE_SIZE
import com.xbot.data.datasource.TitlePagingSource
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek
import com.xbot.domain.repository.TitleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TitleRepositoryImpl @Inject constructor(
    private val dataSource: TitleDataSource
) : TitleRepository {
    override fun getLatestTitles(): Flow<PagingData<TitleModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PAGE_SIZE,
                enablePlaceholders = true,
                jumpThreshold = NETWORK_PAGE_SIZE * 3
            ),
            pagingSourceFactory = { TitlePagingSource(dataSource) }
        ).flow
    }

    override fun getRecommendedTitles(): Flow<List<TitleModel>> {
        return dataSource.getRecommendedTitles(10)
    }

    override fun getScheduleTitles(): Flow<Map<DayOfWeek, List<TitleModel>>> {
        return dataSource.getScheduleTitles()
    }

    override fun getTitle(id: Int): Flow<TitleDetailModel> {
        return dataSource.getTitle(id)
    }
}