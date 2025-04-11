package com.xbot.shared.data.repositories

import androidx.paging.PagingSource
import com.xbot.shared.data.mapper.toApi
import com.xbot.shared.data.mapper.toDayOfWeek
import com.xbot.shared.data.mapper.toDomain
import com.xbot.shared.data.sources.local.CommonPagingSource
import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.api.getCatalogReleases
import com.xbot.shared.data.sources.remote.api.getFranchisesByRelease
import com.xbot.shared.data.sources.remote.api.getRandomGenres
import com.xbot.shared.data.sources.remote.api.getRandomReleases
import com.xbot.shared.data.sources.remote.api.getRelease
import com.xbot.shared.data.sources.remote.api.getScheduleWeek
import com.xbot.shared.data.sources.remote.models.shared.EpisodeApi
import com.xbot.shared.data.sources.remote.models.shared.GenreApi
import com.xbot.shared.data.sources.remote.models.shared.MemberApi
import com.xbot.shared.data.sources.remote.models.shared.ReleaseApi
import com.xbot.shared.domain.models.Genre
import com.xbot.shared.domain.models.Release
import com.xbot.shared.domain.models.ReleaseDetail
import com.xbot.shared.domain.models.enums.AgeRating
import com.xbot.shared.domain.models.enums.AvailabilityStatus
import com.xbot.shared.domain.models.enums.ProductionStatus
import com.xbot.shared.domain.models.enums.PublishStatus
import com.xbot.shared.domain.models.enums.ReleaseType
import com.xbot.shared.domain.models.enums.Season
import com.xbot.shared.domain.models.enums.SortingType
import com.xbot.shared.domain.repository.ReleaseRepository
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
