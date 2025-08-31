package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.EpisodeWithReleaseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

interface EpisodesApi {
    suspend fun getEpisode(episodeId: Int): Either<NetworkError, EpisodeWithReleaseDto>
}

internal class DefaultEpisodesApi(private val client: HttpClient) : EpisodesApi {
    override suspend fun getEpisode(episodeId: Int): Either<NetworkError, EpisodeWithReleaseDto> = client.request {
        get("anime/releases/episodes/${episodeId}")
    }
}