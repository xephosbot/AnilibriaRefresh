package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.EpisodeWithReleaseDto

interface EpisodesApi {
    suspend fun getEpisode(episodeId: Int): Either<NetworkError, EpisodeWithReleaseDto>
    suspend fun getEpisodeTimecode(releaseEpisodeId: String): Either<NetworkError, EpisodeTimecodeDto>
}
