package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.ProductionStatusDto
import com.xbot.network.models.enums.PublishStatusDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SeasonDto
import com.xbot.network.models.enums.SortingTypeDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

interface CatalogApi {
    suspend fun getCatalogReleases(
        page: Int,
        limit: Int,
        genres: List<Int>? = null,
        types: List<ReleaseTypeDto>? = null,
        seasons: List<SeasonDto>? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        search: String? = null,
        sorting: SortingTypeDto? = null,
        ageRatings: List<AgeRatingDto>? = null,
        publishStatuses: List<PublishStatusDto>? = null,
        productionStatuses: List<ProductionStatusDto>? = null,
    ): Either<NetworkError, PaginatedResponse<ReleaseDto>>
    suspend fun getCatalogAgeRatings(): Either<NetworkError, List<AgeRatingDto>>
    suspend fun getCatalogGenres(): Either<NetworkError, List<GenreDto>>
    suspend fun getCatalogProductionStatuses(): Either<NetworkError, List<ProductionStatusDto>>
    suspend fun getCatalogPublishStatuses(): Either<NetworkError, List<PublishStatusDto>>
    suspend fun getCatalogSeasons(): Either<NetworkError, List<SeasonDto>>
    suspend fun getCatalogSortingTypes(): Either<NetworkError, List<SortingTypeDto>>
    suspend fun getCatalogReleaseTypes(): Either<NetworkError, List<ReleaseTypeDto>>
    suspend fun getCatalogYears(): Either<NetworkError, List<Int>>
}

@Singleton
internal class DefaultCatalogApi(private val client: HttpClient) : CatalogApi {
    override suspend fun getCatalogReleases(
        page: Int,
        limit: Int,
        genres: List<Int>?,
        types: List<ReleaseTypeDto>?,
        seasons: List<SeasonDto>?,
        fromYear: Int?,
        toYear: Int?,
        search: String?,
        sorting: SortingTypeDto?,
        ageRatings: List<AgeRatingDto>?,
        publishStatuses: List<PublishStatusDto>?,
        productionStatuses: List<ProductionStatusDto>?
    ): Either<NetworkError, PaginatedResponse<ReleaseDto>> = client.request {
        get("anime/catalog/releases") {
            parameter("page", page)
            parameter("limit", limit)
            genres?.let { parameter("f[genres]", it.joinToString(",")) }
            types?.let { parameter("f[types]", it.joinToString(",")) }
            seasons?.let { parameter("f[seasons]", it.joinToString(",")) }
            fromYear?.let { parameter("f[years][from_year]", it) }
            toYear?.let { parameter("f[years][to_year]", it) }
            search?.let { parameter("f[search]", it) }
            sorting?.let { parameter("f[sorting]", it) }
            ageRatings?.let { parameter("f[age_ratings]", it.joinToString(",")) }
            publishStatuses?.let { parameter("f[publish_statuses]", it.joinToString(",")) }
            productionStatuses?.let { parameter("f[production_statuses]", it.joinToString(",")) }
        }
    }

    override suspend fun getCatalogAgeRatings(): Either<NetworkError, List<AgeRatingDto>> = client.request {
        get("anime/catalog/references/age-ratings")
    }

    override suspend fun getCatalogGenres(): Either<NetworkError, List<GenreDto>> = client.request {
        get("anime/catalog/references/genres")
    }

    override suspend fun getCatalogProductionStatuses(): Either<NetworkError, List<ProductionStatusDto>> = client.request {
        get("anime/catalog/references/production-statuses")
    }

    override suspend fun getCatalogPublishStatuses(): Either<NetworkError, List<PublishStatusDto>> = client.request {
        get("anime/catalog/references/publish-statuses")
    }

    override suspend fun getCatalogSeasons(): Either<NetworkError, List<SeasonDto>> = client.request {
        get("anime/catalog/references/seasons")
    }

    override suspend fun getCatalogSortingTypes(): Either<NetworkError, List<SortingTypeDto>> = client.request {
        get("anime/catalog/references/sorting")
    }

    override suspend fun getCatalogReleaseTypes(): Either<NetworkError, List<ReleaseTypeDto>> = client.request {
        get("anime/catalog/references/types")
    }

    override suspend fun getCatalogYears(): Either<NetworkError, List<Int>> = client.request {
        get("anime/catalog/references/years")
    }
}