package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.EpisodeWithReleaseDto
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultEpisodesApi(private val requester: HttpRequester) : EpisodesApi {
    override suspend fun getEpisode(episodeId: Int): Either<AppError, EpisodeWithReleaseDto> = requester.request {
        get("anime/releases/episodes/${episodeId}")
    }

    override suspend fun getEpisodeTimecode(releaseEpisodeId: String): Either<AppError, EpisodeTimecodeDto> = requester.request {
        get("anime/releases/episodes/${releaseEpisodeId}/timecode")
    }
}
