package com.xbot.domain.usecase

import com.xbot.domain.models.filters.AvailableCatalogFilters
import kotlinx.coroutines.flow.Flow

fun interface GetCatalogFiltersUseCase {
    operator fun invoke(): Flow<AvailableCatalogFilters>
}
