package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.raise.either
import arrow.fx.coroutines.parZip
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.repository.CatalogRepository

class GetCatalogFiltersUseCase(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(): Either<DomainError, CatalogFilters> = either {
        parZip(
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
                ageRatings = ageRatings.bind(),
                genres = genres.bind(),
                productionStatuses = productionStatuses.bind(),
                publishStatuses = publishStatuses.bind(),
                seasons = seasons.bind(),
                sortingTypes = sortingTypes.bind(),
                types = releaseTypes.bind(),
                years = years.bind()
            )
        }
    }
}