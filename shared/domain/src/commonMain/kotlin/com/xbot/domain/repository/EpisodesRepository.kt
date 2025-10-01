package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Episode

interface EpisodesRepository {
    suspend fun getEpisode(episodeId: String): Either<DomainError, Episode>
    suspend fun getEpisodesByRelease(releaseId: Int): Either<DomainError, List<Episode>>
}