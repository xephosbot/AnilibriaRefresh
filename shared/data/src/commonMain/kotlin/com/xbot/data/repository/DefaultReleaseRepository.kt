package com.xbot.data.repository

import androidx.paging.PagingSource
import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.shared.EpisodeApi
import com.xbot.api.models.shared.GenreApi
import com.xbot.api.models.shared.MemberApi
import com.xbot.api.models.shared.ReleaseApi
import com.xbot.api.request.getCatalogReleases
import com.xbot.api.request.getFranchisesByRelease
import com.xbot.api.request.getRandomGenres
import com.xbot.api.request.getRandomReleases
import com.xbot.api.request.getRelease
import com.xbot.api.request.getScheduleWeek
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toApi
import com.xbot.data.mapper.toDayOfWeek
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.repository.ReleaseRepository
import kotlinx.datetime.DayOfWeek

internal class DefaultReleaseRepository(
    private val client: AnilibriaClient
) : ReleaseRepository {
    override fun getReleasePagingSource(
        search: String?,
        genres: List<Genre>?,
        types: List<ReleaseType>?,
        seasons: List<Season>?,
        yearsRange: ClosedRange<Int>?,
        sorting: SortingType?,
        ageRatings: List<AgeRating>?,
        publishStatuses: List<PublishStatus>?,
        productionStatuses: List<ProductionStatus>?
    ): PagingSource<Int, Release> {
        return CommonPagingSource(
            loadPage = { page, limit ->
                val result = client.getCatalogReleases(
                    page = page,
                    limit = limit,
                    search = search,
                    genres = genres?.map(Genre::id),
                    types = types?.map(ReleaseType::toApi),
                    seasons = seasons?.map(Season::toApi),
                    fromYear = yearsRange?.start,
                    toYear = yearsRange?.endInclusive,
                    sorting = sorting?.toApi(),
                    ageRatings = ageRatings?.map(AgeRating::toApi),
                    publishStatuses = publishStatuses?.map(PublishStatus::toApi),
                    productionStatuses = productionStatuses?.map(ProductionStatus::toApi),
                )
                CommonPagingSource.PagedResponse(
                    items = result.data.map(ReleaseApi::toDomain),
                    total = result.meta.pagination.total
                )
            }
        )
    }

    override suspend fun getRecommendedReleases(): Result<List<Release>> = runCatching {
        // просто заглушка выдающая рандомные релизы
        client.getRandomReleases(10).map(ReleaseApi::toDomain)
    }

    override suspend fun getRecommendedGenres(): Result<List<Genre>> = runCatching {
        // просто заглушка выдающая рандомные жанры
        client.getRandomGenres(10).map(GenreApi::toDomain)
    }

    override suspend fun getScheduleWeek(): Result<Map<DayOfWeek, List<Release>>> = runCatching {
        client.getScheduleWeek()
            .map { it.release }
            .groupBy(
                keySelector = { title ->
                    title.publishDay!!.toDayOfWeek()
                },
                valueTransform = ReleaseApi::toDomain,
            ).let { map ->
                map.entries
                    .sortedBy { it.key }
                    .associateBy({ it.key }) { it.value }
            }
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
            members = release.members?.map(MemberApi::toDomain) ?: emptyList(),
            episodes = release.episodes?.map(EpisodeApi::toDomain) ?: emptyList(),
            relatedReleases = relatedReleases.map { it.release.toDomain() }
        )
    }
}
