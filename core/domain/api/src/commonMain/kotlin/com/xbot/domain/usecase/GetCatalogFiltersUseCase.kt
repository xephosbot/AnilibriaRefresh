package com.xbot.domain.usecase

import com.xbot.domain.models.filters.CatalogFilters
import kotlinx.coroutines.flow.Flow

fun interface GetCatalogFiltersUseCase {
    fun invoke(): Flow<CatalogFilters>
}
