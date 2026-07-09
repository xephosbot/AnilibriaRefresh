package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.dto.ReleaseMemberDto
import com.xbot.network.models.responses.PaginatedResponse

interface ReleasesApi {
    suspend fun getLatestReleases(limit: Int): Either<AppError, List<ReleaseDto>>
    suspend fun getRandomReleases(limit: Int): Either<AppError, List<ReleaseDto>>
    suspend fun getReleasesList(
        ids: List<Int>? = null,
        aliases: List<String>? = null,
        page: Int = 1,
        limit: Int = 15
    ): Either<AppError, PaginatedResponse<ReleaseDto>>
    suspend fun getRelease(
        aliasOrId: String
    ): Either<AppError, ReleaseDto>
    suspend fun getReleaseMembers(
        aliasOrId: String
    ): Either<AppError, List<ReleaseMemberDto>>
    suspend fun getReleaseEpisodesTimecodes(
        aliasOrId: String
    ): Either<AppError, List<EpisodeTimecodeDto>>
}
