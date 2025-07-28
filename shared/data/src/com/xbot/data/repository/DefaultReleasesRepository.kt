package com.xbot.data.repository

import androidx.paging.PagingSource
import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.EpisodeApi
import com.xbot.network.models.entities.anime.GenreApi
import com.xbot.network.models.entities.anime.ReleaseMemberApi
import com.xbot.network.models.entities.anime.ReleaseApi
import com.xbot.network.requests.anime.getFranchisesByRelease
import com.xbot.network.requests.anime.getRandomReleases
import com.xbot.network.requests.anime.getRelease
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toDayOfWeek
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.network.requests.anime.getLatestReleases
import com.xbot.network.requests.anime.getReleaseMembers
import com.xbot.network.requests.anime.getReleasesList
import com.xbot.network.requests.app.searchReleases

internal class DefaultReleasesRepository(
    private val client: AnilibriaClient
) : ReleasesRepository {
    override suspend fun getLatestReleases(limit: Int): Result<List<Release>> = runCatching {
        client.getLatestReleases(limit).map(ReleaseApi::toDomain)
    }

    override suspend fun getRandomReleases(limit: Int): Result<List<Release>> = runCatching {
        client.getRandomReleases(limit).map(ReleaseApi::toDomain)
    }

    override fun getReleasesList(
        ids: List<Int>?,
        aliases: List<String>?
    ): PagingSource<Int, Release> {
        return CommonPagingSource(
            loadPage = { page, limit ->
                val result = client.getReleasesList(
                    ids = ids,
                    aliases = aliases,
                    page = page,
                    limit = limit,
                )
                CommonPagingSource.PaginatedResponse(
                    items = result.data.map(ReleaseApi::toDomain),
                    total = result.meta.pagination.total
                )
            }
        )
    }

    override suspend fun getRelease(aliasOrId: String): Result<ReleaseDetail> = runCatching {
        val release = client.getRelease(aliasOrId)
        val relatedReleases = client.getFranchisesByRelease(release.id)
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
            genres = release.genres?.map(GenreApi::toDomain) ?: emptyList(),
            releaseMembers = release.members?.map(ReleaseMemberApi::toDomain) ?: emptyList(),
            episodes = release.episodes?.map(EpisodeApi::toDomain) ?: emptyList(),
            relatedReleases = relatedReleases.map { it.release.toDomain() }
        )
    }

    override suspend fun getReleaseMembers(aliasOrId: String): Result<List<ReleaseMember>> = runCatching {
        client.getReleaseMembers(aliasOrId).map(ReleaseMemberApi::toDomain)
    }

    override suspend fun searchReleases(query: String): Result<List<Release>> = runCatching {
        client.searchReleases(query).map(ReleaseApi::toDomain)
    }
}
