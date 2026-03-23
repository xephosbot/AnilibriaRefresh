package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.dto.ReleaseMemberDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

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

@Singleton
internal class DefaultReleasesApi(private val client: HttpClient) : ReleasesApi {
    override suspend fun getLatestReleases(limit: Int): Either<NetworkError, List<ReleaseDto>> = client.request {
        get("anime/releases/latest") {
            parameter("limit", limit)
        }
    }

    override suspend fun getRandomReleases(limit: Int): Either<NetworkError, List<ReleaseDto>> = client.request {
        get("anime/releases/random") {
            parameter("limit", limit)
        }
    }

    override suspend fun getReleasesList(
        ids: List<Int>?,
        aliases: List<String>?,
        page: Int,
        limit: Int
    ): Either<NetworkError, PaginatedResponse<ReleaseDto>> = client.request {
        get("anime/releases/list") {
            parameter("page", page)
            parameter("limit", limit)
            ids?.let { parameter("ids", it.joinToString(",")) }
            aliases?.let { parameter("aliases", it.joinToString(",")) }
        }
    }

    override suspend fun getRelease(aliasOrId: String): Either<NetworkError, ReleaseDto> = client.request {
        get("anime/releases/${aliasOrId}")
    }

    override suspend fun getReleaseMembers(aliasOrId: String): Either<NetworkError, List<ReleaseMemberDto>> = client.request {
        get("anime/releases/$aliasOrId/members")
    }

    override suspend fun getReleaseEpisodesTimecodes(aliasOrId: String): Either<NetworkError, List<EpisodeTimecodeDto>> = client.request {
        get("anime/releases/$aliasOrId/episodes/timecodes")
    }
}
