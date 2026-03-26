package com.xbot.domain.usecase

import com.xbot.common.DispatcherProvider
import com.xbot.common.combinePartial
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.filters.AvailableCatalogFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetCatalogFiltersUseCase(
    private val catalogRepository: CatalogRepository,
    private val dispatcherProvider: DispatcherProvider,
) : GetCatalogFiltersUseCase {
    override fun invoke(): Flow<AvailableCatalogFilters> = combinePartial(
        { catalogRepository.getCatalogAgeRatings() },
        { catalogRepository.getCatalogGenres() },
        { catalogRepository.getCatalogProductionStatuses() },
        { catalogRepository.getCatalogPublishStatuses() },
        { catalogRepository.getCatalogSeasons() },
        { catalogRepository.getCatalogSortingTypes() },
        { catalogRepository.getCatalogReleaseTypes() },
        { catalogRepository.getCatalogYears() }
    ) { ageRatings, genres, productionStatuses, publishStatuses, seasons, sortingTypes, releaseTypes, years ->
        AvailableCatalogFilters(
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
