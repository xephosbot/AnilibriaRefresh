package com.xbot.domain.usecase

import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.fx.coroutines.parMap
import arrow.fx.coroutines.parZip
import com.xbot.common.DispatcherProvider
import com.xbot.common.combinePartial
import com.xbot.data.repository.CatalogRepository
import com.xbot.data.repository.FranchisesRepository
import com.xbot.data.repository.GenresRepository
import com.xbot.data.repository.ReleasesRepository
import com.xbot.data.repository.ScheduleRepository
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetReleasesFeedUseCase(
    private val releasesRepository: ReleasesRepository,
    private val catalogRepository: CatalogRepository,
    private val scheduleRepository: ScheduleRepository,
    private val franchisesRepository: FranchisesRepository,
    private val genresRepository: GenresRepository,
    private val dispatcherProvider: DispatcherProvider,
) : GetReleasesFeedUseCase {
    override fun invoke(): Flow<ReleasesFeed> = combinePartial(
        { releasesRepository.getRandomReleases(10) },
        { scheduleRepository.getScheduleNow() },
        {
            either {
                val (currentSeason, currentYear) = parZip(
                    { scheduleRepository.getCurrentSeason().bind() },
                    { scheduleRepository.getCurrentYear().bind() }
                ) { season, year -> season to year }

                catalogRepository.getCatalogReleases(
                    search = null,
                    filters = CatalogFilters(
                        seasons = listOf(currentSeason),
                        years = currentYear.let { it..it },
                        sortingTypes = listOf(SortingType.RATING_DESC)
                    ),
                    limit = 10
                ).bind()
            }
        },
        {
            catalogRepository.getCatalogReleases(
                search = null,
                filters = CatalogFilters(sortingTypes = listOf(SortingType.RATING_DESC)),
                limit = 10
            )
        },
        {
            either {
                val franchises = franchisesRepository.getRandomFranchises(10).bind()
                franchises.parMap { franchise ->
                    franchisesRepository.getFranchise(franchise.id).getOrElse { franchise }
                }
            }
        },
        { genresRepository.getRandomGenres(10) }
    ) { recommendedReleases, scheduleNow, bestNow, bestAllTime, recommendedFranchises, genres ->
        ReleasesFeed(
            recommendedReleases = recommendedReleases,
            scheduleNow = scheduleNow,
            bestNow = bestNow,
            bestAllTime = bestAllTime,
            recommendedFranchises = recommendedFranchises,
            genres = genres,
        )
    }.flowOn(dispatcherProvider.io)
}
