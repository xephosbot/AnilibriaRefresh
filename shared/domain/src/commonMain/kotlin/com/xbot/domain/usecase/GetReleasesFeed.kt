package com.xbot.domain.usecase

import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.repository.CatalogRepository
import com.xbot.domain.repository.ReleaseRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetReleasesFeed(
    private val releaseRepository: ReleaseRepository,
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(): Result<ReleasesFeed> = runCatching {
        coroutineScope {
            val recommendedTitles = async { releaseRepository.getRecommendedReleases() }
            val schedule = async { releaseRepository.getScheduleWeek() }
            val genres = async { catalogRepository.getGenres() }

            ReleasesFeed(
                recommendedReleases = recommendedTitles.await().getOrThrow(),
                schedule = schedule.await().getOrThrow(),
                genres = genres.await().getOrThrow().shuffled().take(10)
            )
        }
    }
}