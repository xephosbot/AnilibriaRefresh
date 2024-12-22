package com.xbot.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

class Pager<T : Any>(
    private val pagingSourceFactory: () -> PagingSource<Int, T>
) {
    operator fun invoke(): Flow<PagingData<T>> {
        return Pager<Int, T>(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                enablePlaceholders = true,
                jumpThreshold = PAGE_SIZE * 3,
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}