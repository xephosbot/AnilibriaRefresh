package com.xbot.domain.usecase

import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.repository.ReleaseRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetReleasesFeed(
    private val releaseRepository: ReleaseRepository,
) {
    suspend operator fun invoke(): Result<ReleasesFeed> = runCatching {
        coroutineScope {
            val recommendedTitles = async { releaseRepository.getRecommendedReleases() }
            val schedule = async { releaseRepository.getScheduleWeek() }
            val genres = async { releaseRepository.getRecommendedGenres() }

            ReleasesFeed(
                recommendedReleases = recommendedTitles.await().getOrThrow(),
                schedule = schedule.await().getOrThrow(),
                genres = genres.await().getOrThrow()
            )
        }
    }
}