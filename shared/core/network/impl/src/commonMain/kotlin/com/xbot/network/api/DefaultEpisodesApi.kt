package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.EpisodeWithReleaseDto
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultEpisodesApi(private val requester: ResilientHttpRequester) : EpisodesApi {
    override suspend fun getEpisode(episodeId: Int): Either<DomainError, EpisodeWithReleaseDto> = requester.request {
        get("anime/releases/episodes/${episodeId}")
    }

    override suspend fun getEpisodeTimecode(releaseEpisodeId: String): Either<DomainError, EpisodeTimecodeDto> = requester.request {
        get("anime/releases/episodes/${releaseEpisodeId}/timecode")
    }
}
