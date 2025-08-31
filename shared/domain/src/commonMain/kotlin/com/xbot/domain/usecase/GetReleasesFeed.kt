package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.raise.either
import com.xbot.domain.models.Error
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.repository.GenresRepository
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.domain.repository.ScheduleRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetReleasesFeed(
    private val releasesRepository: ReleasesRepository,
    private val scheduleRepository: ScheduleRepository,
    private val genresRepository: GenresRepository,
) {
    suspend operator fun invoke(): Either<Error, ReleasesFeed> = either {
        coroutineScope {
            val recommendedTitles = async { releasesRepository.getRandomReleases(10) }
            val schedule = async { scheduleRepository.getScheduleWeek() }
            val genres = async { genresRepository.getRandomGenres(10) }

            ReleasesFeed(
                recommendedReleases = recommendedTitles.await().bind(),
                schedule = schedule.await().bind(),
                genres = genres.await().bind()
            )
        }
    }
}