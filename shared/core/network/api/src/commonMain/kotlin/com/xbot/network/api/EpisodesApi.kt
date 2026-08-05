package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.EpisodeWithReleaseDto

interface EpisodesApi {
    suspend fun getEpisode(episodeId: Int): Either<AppError, EpisodeWithReleaseDto>
    suspend fun getEpisodeTimecode(releaseEpisodeId: String): Either<AppError, EpisodeTimecodeDto>
}
