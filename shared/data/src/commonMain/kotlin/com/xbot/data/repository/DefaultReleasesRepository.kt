package com.xbot.data.repository

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import com.xbot.network.models.dto.EpisodeDto
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseMemberDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toDayOfWeek
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Error
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.network.client.NetworkError
import com.xbot.network.api.FranchisesApi
import com.xbot.network.api.ReleasesApi
import com.xbot.network.api.SearchApi

internal class DefaultReleasesRepository(
    private val releasesApi: ReleasesApi,
    private val franchisesApi: FranchisesApi,
    private val searchApi: SearchApi,
) : ReleasesRepository {
    override suspend fun getLatestReleases(limit: Int): Either<Error, List<Release>> = releasesApi
        .getLatestReleases(limit)
        .mapLeft(NetworkError::toDomain)
        .map { it.map(ReleaseDto::toDomain) }

    override suspend fun getRandomReleases(limit: Int): Either<Error, List<Release>> = releasesApi
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

    override suspend fun getRelease(aliasOrId: String): Either<Error, ReleaseDetail> = either {
        val release = releasesApi.getRelease(aliasOrId)
            .mapLeft(NetworkError::toDomain)
            .bind()
        val relatedReleases = franchisesApi.getFranchisesByRelease(release.id)
            .mapLeft(NetworkError::toDomain)
            .bind()
            .flatMap { it.franchiseReleases ?: emptyList() }
            .filterNot { it.release.id == release.id }

        val availabilityStatus = when {
            release.isBlockedByGeo -> AvailabilityStatus.GeoBlocked
            release.isBlockedByCopyrights -> AvailabilityStatus.CopyrightBlocked
            else -> AvailabilityStatus.Available
        }
        ReleaseDetail(
            release = release.toDomain(),
            season = release.season?.toDomain(),
            isOngoing = release.isOngoing,
            publishDay = release.publishDay!!.toDayOfWeek(),
            notification = release.notification,
            availabilityStatus = availabilityStatus,
            genres = release.genres?.map(GenreDto::toDomain) ?: emptyList(),
            releaseMembers = release.members?.map(ReleaseMemberDto::toDomain) ?: emptyList(),
            episodes = release.episodes?.map(EpisodeDto::toDomain) ?: emptyList(),
            relatedReleases = relatedReleases.map { it.release.toDomain() }
        )
    }

    override suspend fun getReleaseMembers(aliasOrId: String): Either<Error, List<ReleaseMember>> = releasesApi
        .getReleaseMembers(aliasOrId)
        .mapLeft(NetworkError::toDomain)
        .map { it.map(ReleaseMemberDto::toDomain) }

    override suspend fun searchReleases(query: String): Either<Error, List<Release>> = searchApi
        .searchReleases(query)
        .mapLeft(NetworkError::toDomain)
        .map { it.map(ReleaseDto::toDomain) }
}
