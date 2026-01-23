package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.EpisodeWithReleaseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

interface EpisodesApi {
    suspend fun getEpisode(episodeId: Int): Either<NetworkError, EpisodeWithReleaseDto>
    suspend fun getEpisodeTimecode(releaseEpisodeId: String): Either<NetworkError, EpisodeTimecodeDto>
}

internal class DefaultEpisodesApi(private val client: HttpClient) : EpisodesApi {
    override suspend fun getEpisode(episodeId: Int): Either<NetworkError, EpisodeWithReleaseDto> = client.request {
        get("anime/releases/episodes/${episodeId}")
    }

    override suspend fun getEpisodeTimecode(releaseEpisodeId: String): Either<NetworkError, EpisodeTimecodeDto> = client.request {
        get("anime/releases/episodes/${releaseEpisodeId}/timecode")
    }
}
