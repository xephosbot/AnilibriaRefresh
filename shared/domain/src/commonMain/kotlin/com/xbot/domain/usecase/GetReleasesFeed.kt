package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.raise.either
import com.xbot.domain.models.Error
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.repository.CatalogRepository
import com.xbot.domain.repository.FranchisesRepository
import com.xbot.domain.repository.GenresRepository
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.domain.repository.ScheduleRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetReleasesFeed(
    private val releasesRepository: ReleasesRepository,
    private val catalogRepository: CatalogRepository,
    private val scheduleRepository: ScheduleRepository,
    private val franchisesRepository: FranchisesRepository,
    private val genresRepository: GenresRepository,
) {
    suspend operator fun invoke(): Either<Error, ReleasesFeed> = either {
        coroutineScope {
            val recommendedTitles = async { releasesRepository.getRandomReleases(10) }
            val scheduleNow = async { scheduleRepository.getScheduleNow() }
            val currentSeason = async { scheduleRepository.getCurrentSeason() }
            val currentYear = async { scheduleRepository.getCurrentYear() }
            val bestNow = async {
                catalogRepository.getCatalogReleases(
                    search = null,
                    filters = CatalogFilters(
                        seasons = listOf(currentSeason.await().bind()),
                        years = currentYear.await().bind().let { it..it },
                        sortingTypes = listOf(SortingType.RATING_DESC)
                    ),
                    limit = 10
                )
            }
            val bestAllTime = async {
                catalogRepository.getCatalogReleases(
                    search = null,
                    filters = CatalogFilters(sortingTypes = listOf(SortingType.RATING_DESC)),
                    limit = 10
                )
            }
            val recommendedFranchises = async { franchisesRepository.getRandomFranchises(10) }
            val genres = async { genresRepository.getRandomGenres(10) }

            ReleasesFeed(
                recommendedReleases = recommendedTitles.await().bind(),
                scheduleNow = scheduleNow.await().bind(),
                bestNow = bestNow.await().bind(),
                bestAllTime = bestAllTime.await().bind(),
                recommendedFranchises = recommendedFranchises.await().bind(),
                genres = genres.await().bind(),
            )
        }
    }
}