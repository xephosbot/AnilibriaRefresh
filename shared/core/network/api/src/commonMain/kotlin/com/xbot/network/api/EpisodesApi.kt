package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.EpisodeWithReleaseDto

interface EpisodesApi {
    suspend fun getEpisode(episodeId: Int): Either<DomainError, EpisodeWithReleaseDto>
    suspend fun getEpisodeTimecode(releaseEpisodeId: String): Either<DomainError, EpisodeTimecodeDto>
}
