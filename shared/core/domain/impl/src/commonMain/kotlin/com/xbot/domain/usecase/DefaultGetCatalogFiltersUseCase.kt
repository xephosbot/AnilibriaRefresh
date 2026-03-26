package com.xbot.domain.usecase

import com.xbot.common.DispatcherProvider
import com.xbot.common.combinePartial
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.filters.CatalogFilters
import io.nlopez.asyncresult.getOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetCatalogFiltersUseCase(
    private val catalogRepository: CatalogRepository,
    private val dispatcherProvider: DispatcherProvider,
) : GetCatalogFiltersUseCase {
    override fun invoke(): Flow<CatalogFilters> = combinePartial(
        { catalogRepository.getCatalogAgeRatings() },
        { catalogRepository.getCatalogGenres() },
        { catalogRepository.getCatalogProductionStatuses() },
        { catalogRepository.getCatalogPublishStatuses() },
        { catalogRepository.getCatalogSeasons() },
        { catalogRepository.getCatalogSortingTypes() },
        { catalogRepository.getCatalogReleaseTypes() },
        { catalogRepository.getCatalogYears() }
    ) { ageRatings, genres, productionStatuses, publishStatuses, seasons, sortingTypes, releaseTypes, years ->
        CatalogFilters(
            ageRatings = ageRatings.getOrNull() ?: emptyList(),
            genres = genres.getOrNull() ?: emptyList(),
            productionStatuses = productionStatuses.getOrNull() ?: emptyList(),
            publishStatuses = publishStatuses.getOrNull() ?: emptyList(),
            seasons = seasons.getOrNull() ?: emptyList(),
            sortingTypes = sortingTypes.getOrNull() ?: emptyList(),
            types = releaseTypes.getOrNull() ?: emptyList(),
            years = years.getOrNull() ?: IntRange.EMPTY
        )
    }.flowOn(dispatcherProvider.io)
}
