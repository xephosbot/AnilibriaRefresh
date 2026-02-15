package com.xbot.data.repository

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.getOrElse
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toDomain
import com.xbot.data.mapper.toReleaseDetails
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.network.api.ReleasesApi
import com.xbot.network.api.SearchApi
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.dto.ReleaseMemberDto

internal class DefaultReleasesRepository(
    private val releasesApi: ReleasesApi,
    private val searchApi: SearchApi,
) : ReleasesRepository {
    override suspend fun getLatestReleases(limit: Int): Either<DomainError, List<Release>> = releasesApi
        .getLatestReleases(limit)
        .mapLeft(NetworkError::toDomain)
        .map { it.map(ReleaseDto::toDomain) }

    override suspend fun getRandomReleases(limit: Int): Either<DomainError, List<Release>> = releasesApi
        .getRandomReleases(limit)
        .mapLeft(NetworkError::toDomain)
        .map { it.map(ReleaseDto::toDomain) }

    override fun getReleasesList(
        ids: List<Int>?,
        aliases: List<String>?
    ): PagingSource<Int, Release> {
        return CommonPagingSource(
            loadPage = { page, limit ->
                val result = releasesApi.getReleasesList(
                    ids = ids,
                    aliases = aliases,
                    page = page,
                    limit = limit,
                ).getOrElse { error ->
                    throw IllegalStateException()
                }
                CommonPagingSource.PaginatedResponse(
                    items = result.data.map(ReleaseDto::toDomain),
                    total = result.meta.pagination.total
                )
            }
        )
    }

    override suspend fun getRelease(aliasOrId: String): Either<DomainError, ReleaseDetails> = releasesApi
        .getRelease(aliasOrId)
        .mapLeft(NetworkError::toDomain)
        .map(ReleaseDto::toReleaseDetails)

    override suspend fun getReleaseMembers(aliasOrId: String): Either<DomainError, List<ReleaseMember>> = releasesApi
        .getReleaseMembers(aliasOrId)
        .mapLeft(NetworkError::toDomain)
        .map { it.map(ReleaseMemberDto::toDomain) }

    override suspend fun searchReleases(query: String): Either<DomainError, List<Release>> = searchApi
        .searchReleases(query)
        .mapLeft(NetworkError::toDomain)
        .map { it.map(ReleaseDto::toDomain) }
}
