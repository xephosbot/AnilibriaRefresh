package com.xbot.fixtures.repository

import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Episode
import com.xbot.domain.repository.EpisodesRepository
import com.xbot.fixtures.data.episodeMocks

class FakeEpisodesRepository : EpisodesRepository {
    override suspend fun getEpisode(episodeId: String): Either<DomainError, Episode> {
        val episode = episodeMocks.find { it.id == episodeId } ?: episodeMocks.first()
        return episode.right()
    }

    override suspend fun getEpisodesByRelease(releaseId: Int): Either<DomainError, List<Episode>> {
        return episodeMocks.right()
    }
}
