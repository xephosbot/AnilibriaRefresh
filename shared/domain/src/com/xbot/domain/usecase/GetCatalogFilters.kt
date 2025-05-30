package com.xbot.domain.usecase

import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.repository.CatalogRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetCatalogFilters(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(): Result<CatalogFilters> = runCatching {
        coroutineScope {
            val ageRatings = async { catalogRepository.getCatalogAgeRatings() }
            val genres = async { catalogRepository.getCatalogGenres() }
            val productionStatuses = async { catalogRepository.getCatalogProductionStatuses() }
            val publishStatuses = async { catalogRepository.getCatalogPublishStatuses() }
            val seasons = async { catalogRepository.getCatalogSeasons() }
            val sortingTypes = async { catalogRepository.getCatalogSortingTypes() }
            val releaseTypes = async { catalogRepository.getCatalogReleaseTypes() }
            val years = async { catalogRepository.getCatalogYears() }

            CatalogFilters(
                ageRatings = ageRatings.await().getOrThrow(),
                genres = genres.await().getOrThrow(),
                productionStatuses = productionStatuses.await().getOrThrow(),
                publishStatuses = publishStatuses.await().getOrThrow(),
                seasons = seasons.await().getOrThrow(),
                sortingTypes = sortingTypes.await().getOrThrow(),
                types = releaseTypes.await().getOrThrow(),
                years = years.await().getOrThrow()
            )
        }
    }
}