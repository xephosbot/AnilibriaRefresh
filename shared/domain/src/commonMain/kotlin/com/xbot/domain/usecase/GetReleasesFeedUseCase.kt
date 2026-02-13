package com.xbot.domain.usecase

import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.fx.coroutines.parMap
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.repository.CatalogRepository
import com.xbot.domain.repository.FranchisesRepository
import com.xbot.domain.repository.GenresRepository
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.domain.repository.ScheduleRepository
import com.xbot.domain.utils.DispatcherProvider
import com.xbot.domain.utils.combinePartial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetReleasesFeedUseCase(
    private val releasesRepository: ReleasesRepository,
    private val catalogRepository: CatalogRepository,
    private val scheduleRepository: ScheduleRepository,
    private val franchisesRepository: FranchisesRepository,
    private val genresRepository: GenresRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(): Flow<ReleasesFeed> = combinePartial(
        { releasesRepository.getRandomReleases(10).getOrNull() },
        { scheduleRepository.getScheduleNow().getOrNull() },
        {
            either {
                val currentSeason = scheduleRepository.getCurrentSeason().bind()
                val currentYear = scheduleRepository.getCurrentYear().bind()
                catalogRepository.getCatalogReleases(
                    search = null,
                    filters = CatalogFilters(
                        seasons = listOf(currentSeason),
                        years = currentYear.let { it..it },
                        sortingTypes = listOf(SortingType.RATING_DESC)
                    ),
                    limit = 10
                ).bind()
            }.getOrNull()
        },
        {
            catalogRepository.getCatalogReleases(
                search = null,
                filters = CatalogFilters(sortingTypes = listOf(SortingType.RATING_DESC)),
                limit = 10
            ).getOrNull()
        },
        {
            either {
                val franchises = franchisesRepository.getRandomFranchises(10).bind()
                franchises.parMap { franchise ->
                    franchisesRepository.getFranchise(franchise.id).getOrElse { franchise }
                }
            }.getOrNull()
        },
        { genresRepository.getRandomGenres(10).getOrNull() }
    ) { recommendedReleases, scheduleNow, bestNow, bestAllTime, recommendedFranchises, genres ->
        ReleasesFeed.create(
            recommendedReleases = recommendedReleases,
            scheduleNow = scheduleNow,
            bestNow = bestNow,
            bestAllTime = bestAllTime,
            recommendedFranchises = recommendedFranchises,
            genres = genres,
        )
    }.flowOn(dispatcherProvider.io)
}
