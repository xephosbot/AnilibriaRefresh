package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.EpisodeWithReleaseDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface EpisodesApi {
    @GET("anime/releases/episodes/{episodeId}")
    suspend fun getEpisode(
        @Path("episodeId") episodeId: Int
    ): Either<AppError, EpisodeWithReleaseDto>

    @GET("anime/releases/episodes/{releaseEpisodeId}/timecode")
    suspend fun getEpisodeTimecode(
        @Path("releaseEpisodeId") releaseEpisodeId: String
    ): Either<AppError, EpisodeTimecodeDto>
}
