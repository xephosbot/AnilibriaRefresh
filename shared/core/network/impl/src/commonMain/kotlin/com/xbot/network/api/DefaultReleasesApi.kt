package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.dto.ReleaseMemberDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultReleasesApi(private val requester: ResilientHttpRequester) : ReleasesApi {
    override suspend fun getLatestReleases(limit: Int): Either<DomainError, List<ReleaseDto>> = requester.request {
        get("anime/releases/latest") {
            parameter("limit", limit)
        }
    }

    override suspend fun getRandomReleases(limit: Int): Either<DomainError, List<ReleaseDto>> = requester.request {
        get("anime/releases/random") {
            parameter("limit", limit)
        }
    }

    override suspend fun getReleasesList(
        ids: List<Int>?,
        aliases: List<String>?,
        page: Int,
        limit: Int
    ): Either<DomainError, PaginatedResponse<ReleaseDto>> = requester.request {
        get("anime/releases/list") {
            parameter("page", page)
            parameter("limit", limit)
            ids?.let { parameter("ids", it.joinToString(",")) }
            aliases?.let { parameter("aliases", it.joinToString(",")) }
        }
    }

    override suspend fun getRelease(aliasOrId: String): Either<DomainError, ReleaseDto> = requester.request {
        get("anime/releases/${aliasOrId}")
    }

    override suspend fun getReleaseMembers(aliasOrId: String): Either<DomainError, List<ReleaseMemberDto>> = requester.request {
        get("anime/releases/$aliasOrId/members")
    }

    override suspend fun getReleaseEpisodesTimecodes(aliasOrId: String): Either<DomainError, List<EpisodeTimecodeDto>> = requester.request {
        get("anime/releases/$aliasOrId/episodes/timecodes")
    }
}
