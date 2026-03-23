package com.xbot.domain.usecase

import androidx.paging.PagingData
import com.xbot.domain.models.Release
import com.xbot.domain.models.filters.CatalogFilters
import kotlinx.coroutines.flow.Flow

fun interface GetCatalogReleasesPagerUseCase {
    fun invoke(
        search: String?,
        filters: CatalogFilters?
    ): Flow<PagingData<Release>>
}
