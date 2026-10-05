package com.xbot.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xbot.domain.models.Release
import com.xbot.domain.models.filters.CatalogQuery
import com.xbot.domain.repository.CatalogRepository
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetCatalogReleasesUseCase(private val catalogRepository: CatalogRepository) :
    FlowUseCase<GetCatalogReleasesUseCase.Params, PagingData<Release>> {
    data class Params(val search: String? = null, val filters: CatalogQuery? = null)

    override fun invoke(params: Params): Flow<PagingData<Release>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = PAGE_SIZE,
            jumpThreshold = PAGE_SIZE * 3
        ),
        pagingSourceFactory = {
            catalogRepository.getCatalogReleases(
                search = params.search,
                filters = params.filters
            )
        }
    ).flow

    private companion object {
        const val PAGE_SIZE = 20
    }
}
