package com.xbot.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.Release
import com.xbot.domain.models.filters.CatalogFilters
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetCatalogReleasesPagerUseCase(
    private val catalogRepository: CatalogRepository
) : GetCatalogReleasesPagerUseCase {
    override fun invoke(
        search: String?,
        filters: CatalogFilters?
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