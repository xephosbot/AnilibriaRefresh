package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.raise.context.bind
import arrow.core.raise.context.either
import arrow.fx.coroutines.parZip
import com.xbot.common.error.AppError
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogQuery
import com.xbot.domain.repository.CatalogRepository
import com.xbot.domain.repository.ScheduleRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetBestReleasesInCurrentSeasonUseCase(
    private val catalogRepository: CatalogRepository,
    private val scheduleRepository: ScheduleRepository
) : EitherUseCase<Unit, List<Release>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<Release>> = either {
        val (currentSeason, currentYear) = parZip(
            { scheduleRepository.getCurrentSeason().bind() },
            { scheduleRepository.getCurrentYear().bind() }
        ) { season, year -> season to year }

        catalogRepository.getCatalogReleases(
            search = null,
            filters = CatalogQuery(
                seasons = listOf(currentSeason),
                years = currentYear.let { it..it },
                sortingTypes = listOf(SortingType.RATING_DESC)
            ),
            limit = 10
        ).bind()
    }
}
