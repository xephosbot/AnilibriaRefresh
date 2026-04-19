package com.xbot.data.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toDomain
import com.xbot.data.mapper.toDto
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogQuery
import com.xbot.network.api.CatalogApi
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.ProductionStatusDto
import com.xbot.network.models.enums.PublishStatusDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SeasonDto
import com.xbot.network.models.enums.SortingTypeDto
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultCatalogRepository(
    private val catalogApi: CatalogApi
) : CatalogRepository {
    override fun getCatalogReleases(search: String?, filters: CatalogQuery?): PagingSource<Int, Release> {
        return CommonPagingSource(
            loadPage = { page, limit ->
                catalogApi.getCatalogReleases(
                    page = page,
                    limit = limit,
                    search = search,
                    genres = filters?.genres?.map(Genre::id),
                    types = filters?.types?.map(ReleaseType::toDto),
                    seasons = filters?.seasons?.map(Season::toDto),
                    fromYear = filters?.years?.takeIf { it != IntRange.EMPTY }?.start,
                    toYear = filters?.years?.takeIf { it != IntRange.EMPTY }?.endInclusive,
                    sorting = filters?.sortingTypes?.firstOrNull()?.toDto(),
                    ageRatings = filters?.ageRatings?.map(AgeRating::toDto),
                    publishStatuses = filters?.publishStatuses?.map(PublishStatus::toDto),
                    productionStatuses = filters?.productionStatuses?.map(ProductionStatus::toDto),
                ).map { result ->
                    CommonPagingSource.PaginatedResponse(
                        items = result.data.map(ReleaseDto::toDomain),
                        total = result.meta.pagination.total,
                    )
                }
            }
        )
    }

    override suspend fun getCatalogReleases(
        search: String?,
        filters: CatalogQuery?,
        limit: Int
    ): Either<DomainError, List<Release>> = catalogApi
        .getCatalogReleases(
            page = 1,
            limit = limit,
            search = search,
            genres = filters?.genres?.map(Genre::id),
            types = filters?.types?.map(ReleaseType::toDto),
            seasons = filters?.seasons?.map(Season::toDto),
            fromYear = filters?.years?.takeIf { it != IntRange.EMPTY }?.start,
            toYear = filters?.years?.takeIf { it != IntRange.EMPTY }?.endInclusive,
            sorting = filters?.sortingTypes?.firstOrNull()?.toDto(),
            ageRatings = filters?.ageRatings?.map(AgeRating::toDto),
            publishStatuses = filters?.publishStatuses?.map(PublishStatus::toDto),
            productionStatuses = filters?.productionStatuses?.map(ProductionStatus::toDto),
        )
        .map { it.data.map(ReleaseDto::toDomain) }

    override suspend fun getCatalogAgeRatings(): Either<DomainError, List<AgeRating>> = catalogApi
        .getCatalogAgeRatings()
        .map { it.map(AgeRatingDto::toDomain) }

    override suspend fun getCatalogGenres(): Either<DomainError, List<Genre>> = catalogApi
        .getCatalogGenres()
        .map { it.map(GenreDto::toDomain) }

    override suspend fun getCatalogProductionStatuses(): Either<DomainError, List<ProductionStatus>> =
        catalogApi
            .getCatalogProductionStatuses()
            .map { it.map(ProductionStatusDto::toDomain) }

    override suspend fun getCatalogPublishStatuses(): Either<DomainError, List<PublishStatus>> =
        catalogApi
            .getCatalogPublishStatuses()
            .map { it.map(PublishStatusDto::toDomain) }

    override suspend fun getCatalogSeasons(): Either<DomainError, List<Season>> = catalogApi
        .getCatalogSeasons()
        .map { it.map(SeasonDto::toDomain) }

    override suspend fun getCatalogSortingTypes(): Either<DomainError, List<SortingType>> = catalogApi
        .getCatalogSortingTypes()
        .map { it.map(SortingTypeDto::toDomain) }

    override suspend fun getCatalogReleaseTypes(): Either<DomainError, List<ReleaseType>> = catalogApi
        .getCatalogReleaseTypes()
        .map { it.map(ReleaseTypeDto::toDomain) }

    override suspend fun getCatalogYears(): Either<DomainError, IntRange> = catalogApi
        .getCatalogYears()
        .map { years -> years.first()..years.last() }
}
