package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.raise.either
import com.xbot.domain.models.Error
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.repository.CatalogRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetCatalogFilters(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(): Either<Error, CatalogFilters> = either {
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
                ageRatings = ageRatings.await().bind(),
                genres = genres.await().bind(),
                productionStatuses = productionStatuses.await().bind(),
                publishStatuses = publishStatuses.await().bind(),
                seasons = seasons.await().bind(),
                sortingTypes = sortingTypes.await().bind(),
                types = releaseTypes.await().bind(),
                years = years.await().bind()
            )
        }
    }
}