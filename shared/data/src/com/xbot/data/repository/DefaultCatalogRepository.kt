package com.xbot.data.repository

import androidx.paging.PagingSource
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toApi
import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.GenreApi
import com.xbot.network.models.enums.AgeRatingApi
import com.xbot.network.models.enums.ProductionStatusApi
import com.xbot.network.models.enums.PublishStatusApi
import com.xbot.network.models.enums.ReleaseTypeApi
import com.xbot.network.models.enums.SeasonApi
import com.xbot.network.models.enums.SortingTypeApi
import com.xbot.network.requests.anime.getCatalogAgeRatings
import com.xbot.network.requests.anime.getCatalogGenres
import com.xbot.network.requests.anime.getCatalogProductionStatuses
import com.xbot.network.requests.anime.getCatalogPublishStatuses
import com.xbot.network.requests.anime.getCatalogReleaseTypes
import com.xbot.network.requests.anime.getCatalogSeasons
import com.xbot.network.requests.anime.getCatalogSortingTypes
import com.xbot.network.requests.anime.getCatalogYears
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.repository.CatalogRepository
import com.xbot.network.models.entities.anime.ReleaseApi
import com.xbot.network.requests.anime.getCatalogReleases

internal class DefaultCatalogRepository(
    private val client: AnilibriaClient
) : CatalogRepository {
    override fun getCatalogReleases(search: String?, filters: CatalogFilters?): PagingSource<Int, Release> {
        return CommonPagingSource(
            loadPage = { page, limit ->
                val result = client.getCatalogReleases(
                    page = page,
                    limit = limit,
                    search = search,
                    genres = filters?.genres?.map(Genre::id),
                    types = filters?.types?.map(ReleaseType::toApi),
                    seasons = filters?.seasons?.map(Season::toApi),
                    fromYear = filters?.years?.start,
                    toYear = filters?.years?.endInclusive,
                    sorting = filters?.sortingTypes?.firstOrNull()?.toApi(),
                    ageRatings = filters?.ageRatings?.map(AgeRating::toApi),
                    publishStatuses = filters?.publishStatuses?.map(PublishStatus::toApi),
                    productionStatuses = filters?.productionStatuses?.map(ProductionStatus::toApi),
                )
                CommonPagingSource.PaginatedResponse(
                    items = result.data.map(ReleaseApi::toDomain),
                    total = result.meta.pagination.total
                )
            }
        )
    }
    override suspend fun getCatalogAgeRatings(): Result<List<AgeRating>> = runCatching {
        client.getCatalogAgeRatings().map(AgeRatingApi::toDomain)
    }

    override suspend fun getCatalogGenres(): Result<List<Genre>> = runCatching {
        client.getCatalogGenres().map(GenreApi::toDomain)
    }

    override suspend fun getCatalogProductionStatuses(): Result<List<ProductionStatus>> = runCatching {
        client.getCatalogProductionStatuses().map(ProductionStatusApi::toDomain)
    }

    override suspend fun getCatalogPublishStatuses(): Result<List<PublishStatus>> = runCatching {
        client.getCatalogPublishStatuses().map(PublishStatusApi::toDomain)
    }

    override suspend fun getCatalogSeasons(): Result<List<Season>> = runCatching {
        client.getCatalogSeasons().map(SeasonApi::toDomain)
    }

    override suspend fun getCatalogSortingTypes(): Result<List<SortingType>> = runCatching {
        client.getCatalogSortingTypes().map(SortingTypeApi::toDomain)
    }

    override suspend fun getCatalogReleaseTypes(): Result<List<ReleaseType>> = runCatching {
        client.getCatalogReleaseTypes().map(ReleaseTypeApi::toDomain)
    }

    override suspend fun getCatalogYears(): Result<ClosedRange<Int>> = runCatching {
        client.getCatalogYears().let { years ->
            years.first()..years.last()
        }
    }
}
