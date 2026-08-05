package com.xbot.data.repository

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Episode

interface EpisodesRepository {
    suspend fun getEpisode(episodeId: String): Either<AppError, Episode>
    suspend fun getEpisodesByRelease(releaseId: Int): Either<AppError, List<Episode>>
}