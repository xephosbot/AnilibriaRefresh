package com.xbot.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xbot.domain.models.Release
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow

class GetCatalogReleasesPagerUseCase(
    private val catalogRepository: CatalogRepository
) {
    operator fun invoke(
        search: String? = null,
        filters: CatalogFilters? = null
    ): Flow<PagingData<Release>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = PAGE_SIZE,
                jumpThreshold = PAGE_SIZE * 3,
            ),
            pagingSourceFactory = {
                catalogRepository.getCatalogReleases(
                    search = search,
                    filters = filters
                )
            }
        ).flow
    }

    companion object Companion {
        const val PAGE_SIZE = 20
    }
}