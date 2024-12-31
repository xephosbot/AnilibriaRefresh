package com.xbot.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.xbot.domain.models.Release
import com.xbot.domain.repository.ReleaseRepository
import kotlinx.coroutines.flow.Flow

class GetReleasesPager(
    private val releaseRepository: ReleaseRepository
) {
    @NativeCoroutines
    operator fun invoke(): Flow<PagingData<Release>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = PAGE_SIZE,
                jumpThreshold = PAGE_SIZE * 3,
            ),
            pagingSourceFactory = releaseRepository::getReleasePagingSource
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}