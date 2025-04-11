package com.xbot.shared.domain.usecase

import com.xbot.shared.domain.models.CatalogFilters
import com.xbot.shared.domain.repository.CatalogRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetCatalogFilters(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(): Result<CatalogFilters> = runCatching {
        coroutineScope {
            val ageRatings = async { catalogRepository.getAgeRatings() }
            val genres = async { catalogRepository.getGenres() }
            val productionStatuses = async { catalogRepository.getProductionStatuses() }
            val publishStatuses = async { catalogRepository.getPublishStatuses() }
            val seasons = async { catalogRepository.getSeasons() }
            val sortingTypes = async { catalogRepository.getSortingTypes() }
            val releaseTypes = async { catalogRepository.getReleaseTypes() }
            val years = async { catalogRepository.getYears() }

            CatalogFilters(
                ageRatings = ageRatings.await().getOrThrow(),
                genres = genres.await().getOrThrow(),
                productionStatuses = productionStatuses.await().getOrThrow(),
                publishStatuses = publishStatuses.await().getOrThrow(),
                seasons = seasons.await().getOrThrow(),
                sortingTypes = sortingTypes.await().getOrThrow(),
                releaseTypes = releaseTypes.await().getOrThrow(),
                years = years.await().getOrThrow()
            )
        }
    }
}