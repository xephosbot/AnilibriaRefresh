package com.xbot.domain.usecase

import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.repository.CatalogRepository
import com.xbot.domain.utils.DispatcherProvider
import com.xbot.domain.utils.combinePartial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
class GetCatalogFiltersUseCase(
    private val catalogRepository: CatalogRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(): Flow<CatalogFilters> = combinePartial(
        { catalogRepository.getCatalogAgeRatings() },
        { catalogRepository.getCatalogGenres() },
        { catalogRepository.getCatalogProductionStatuses() },
        { catalogRepository.getCatalogPublishStatuses() },
        { catalogRepository.getCatalogSeasons() },
        { catalogRepository.getCatalogSortingTypes() },
        { catalogRepository.getCatalogReleaseTypes() },
        { catalogRepository.getCatalogYears() }
    ) { ageRatings, genres, productionStatuses, publishStatuses, seasons, sortingTypes, releaseTypes, years ->
        CatalogFilters.create(
            ageRatings = ageRatings,
            genres = genres,
            productionStatuses = productionStatuses,
            publishStatuses = publishStatuses,
            seasons = seasons,
            sortingTypes = sortingTypes,
            types = releaseTypes,
            years = years
        )
    }.flowOn(dispatcherProvider.io)
}
