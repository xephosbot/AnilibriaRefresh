package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.ProductionStatusDto
import com.xbot.network.models.enums.PublishStatusDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SeasonDto
import com.xbot.network.models.enums.SortingTypeDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultCatalogApi(private val requester: HttpRequester) : CatalogApi {
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
    ): Either<AppError, PaginatedResponse<ReleaseDto>> = requester.request {
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

    override suspend fun getCatalogAgeRatings(): Either<AppError, List<AgeRatingDto>> = requester.request {
        get("anime/catalog/references/age-ratings")
    }

    override suspend fun getCatalogGenres(): Either<AppError, List<GenreDto>> = requester.request {
        get("anime/catalog/references/genres")
    }

    override suspend fun getCatalogProductionStatuses(): Either<AppError, List<ProductionStatusDto>> = requester.request {
        get("anime/catalog/references/production-statuses")
    }

    override suspend fun getCatalogPublishStatuses(): Either<AppError, List<PublishStatusDto>> = requester.request {
        get("anime/catalog/references/publish-statuses")
    }

    override suspend fun getCatalogSeasons(): Either<AppError, List<SeasonDto>> = requester.request {
        get("anime/catalog/references/seasons")
    }

    override suspend fun getCatalogSortingTypes(): Either<AppError, List<SortingTypeDto>> = requester.request {
        get("anime/catalog/references/sorting")
    }

    override suspend fun getCatalogReleaseTypes(): Either<AppError, List<ReleaseTypeDto>> = requester.request {
        get("anime/catalog/references/types")
    }

    override suspend fun getCatalogYears(): Either<AppError, List<Int>> = requester.request {
        get("anime/catalog/references/years")
    }
}
