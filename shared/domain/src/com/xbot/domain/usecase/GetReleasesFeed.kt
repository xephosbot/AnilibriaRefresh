package com.xbot.domain.usecase

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
    suspend operator fun invoke(): Result<ReleasesFeed> = runCatching {
        coroutineScope {
            val recommendedTitles = async { releasesRepository.getRandomReleases(10) }
            val schedule = async { scheduleRepository.getScheduleWeek() }
            val genres = async { genresRepository.getRandomGenres(10) }

            ReleasesFeed(
                recommendedReleases = recommendedTitles.await().getOrThrow(),
                schedule = schedule.await().getOrThrow(),
                genres = genres.await().getOrThrow()
            )
        }
    }
}