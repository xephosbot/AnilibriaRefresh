package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.raise.either
import arrow.fx.coroutines.parZip
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.repository.CatalogRepository
import com.xbot.domain.repository.FranchisesRepository
import com.xbot.domain.repository.GenresRepository
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.domain.repository.ScheduleRepository

class GetReleasesFeed(
    private val releasesRepository: ReleasesRepository,
    private val catalogRepository: CatalogRepository,
    private val scheduleRepository: ScheduleRepository,
    private val franchisesRepository: FranchisesRepository,
    private val genresRepository: GenresRepository,
) {
    suspend operator fun invoke(): Either<DomainError, ReleasesFeed> = either {
        val currentSeason = scheduleRepository.getCurrentSeason().bind()
        val currentYear = scheduleRepository.getCurrentYear().bind()

        parZip(
            { releasesRepository.getRandomReleases(10) },
            { scheduleRepository.getScheduleNow() },
            {
                catalogRepository.getCatalogReleases(
                    search = null,
                    filters = CatalogFilters(
                        seasons = listOf(currentSeason),
                        years = currentYear.let { it..it },
                        sortingTypes = listOf(SortingType.RATING_DESC)
                    ),
                    limit = 10
                )
            },
            {
                catalogRepository.getCatalogReleases(
                    search = null,
                    filters = CatalogFilters(sortingTypes = listOf(SortingType.RATING_DESC)),
                    limit = 10
                )
            },
            { franchisesRepository.getRandomFranchises(10) },
            { genresRepository.getRandomGenres(10) }
        ) { recommendedTitles, scheduleNow, bestNow, bestAllTime, recommendedFranchises, genres ->
            ReleasesFeed(
                recommendedReleases = recommendedTitles.bind(),
                scheduleNow = scheduleNow.bind(),
                bestNow = bestNow.bind(),
                bestAllTime = bestAllTime.bind(),
                recommendedFranchises = recommendedFranchises.bind(),
                genres = genres.bind(),
            )
        }
    }
}