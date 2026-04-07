package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.raise.context.bind
import arrow.core.raise.context.either
import arrow.fx.coroutines.parZip
import com.xbot.data.repository.CatalogRepository
import com.xbot.data.repository.ScheduleRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogFilters
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetBestReleasesInCurrentSeasonUseCase(
    private val catalogRepository: CatalogRepository,
    private val scheduleRepository: ScheduleRepository,
) : GetBestReleasesInCurrentSeasonUseCase {
    override suspend fun invoke(): Either<DomainError, List<Release>> = either {
        val (currentSeason, currentYear) = parZip(
            { scheduleRepository.getCurrentSeason().bind() },
            { scheduleRepository.getCurrentYear().bind() }
        ) { season, year -> season to year }

        catalogRepository.getCatalogReleases(
            search = null,
            filters = CatalogFilters.create(
                seasons = listOf(currentSeason),
                years = currentYear.let { it..it },
                sortingTypes = listOf(SortingType.RATING_DESC)
            ),
            limit = 10
        ).bind()
    }
}
