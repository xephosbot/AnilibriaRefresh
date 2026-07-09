package com.xbot.data.fixtures

import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.EpisodesRepository
import com.xbot.domain.fixtures.EpisodeFixtures
import com.xbot.common.error.AppError
import com.xbot.domain.models.Episode

class FakeEpisodesRepository : EpisodesRepository {
    override suspend fun getEpisode(episodeId: String): Either<AppError, Episode> {
        val episode = EpisodeFixtures.all.find { it.id == episodeId } ?: EpisodeFixtures.episode1
        return episode.right()
    }

    override suspend fun getEpisodesByRelease(releaseId: Int): Either<AppError, List<Episode>> {
        return EpisodeFixtures.all.right()
    }
}
