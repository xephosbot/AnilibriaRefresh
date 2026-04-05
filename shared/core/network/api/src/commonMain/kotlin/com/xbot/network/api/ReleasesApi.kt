package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.dto.ReleaseMemberDto
import com.xbot.network.models.responses.PaginatedResponse

interface ReleasesApi {
    suspend fun getLatestReleases(limit: Int): Either<NetworkError, List<ReleaseDto>>
    suspend fun getRandomReleases(limit: Int): Either<NetworkError, List<ReleaseDto>>
    suspend fun getReleasesList(
        ids: List<Int>? = null,
        aliases: List<String>? = null,
        page: Int = 1,
        limit: Int = 15
    ): Either<NetworkError, PaginatedResponse<ReleaseDto>>
    suspend fun getRelease(
        aliasOrId: String
    ): Either<NetworkError, ReleaseDto>
    suspend fun getReleaseMembers(
        aliasOrId: String
    ): Either<NetworkError, List<ReleaseMemberDto>>
    suspend fun getReleaseEpisodesTimecodes(
        aliasOrId: String
    ): Either<NetworkError, List<EpisodeTimecodeDto>>
}
