package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.ProductionStatusDto
import com.xbot.network.models.enums.PublishStatusDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SeasonDto
import com.xbot.network.models.enums.SortingTypeDto
import com.xbot.network.models.responses.PaginatedResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface CatalogApi {
    @GET("anime/catalog/releases")
    suspend fun getCatalogReleases(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("f[genres][]") genres: List<Int>? = null,
        @Query("f[types][]") types: List<ReleaseTypeDto>? = null,
        @Query("f[seasons][]") seasons: List<SeasonDto>? = null,
        @Query("f[years][from_year]") fromYear: Int? = null,
        @Query("f[years][to_year]") toYear: Int? = null,
        @Query("f[search]") search: String? = null,
        @Query("f[sorting]") sorting: SortingTypeDto? = null,
        @Query("f[age_ratings][]") ageRatings: List<AgeRatingDto>? = null,
        @Query("f[publish_statuses][]") publishStatuses: List<PublishStatusDto>? = null,
        @Query("f[production_statuses][]") productionStatuses: List<ProductionStatusDto>? = null
    ): Either<AppError, PaginatedResponse<ReleaseDto>>

    @GET("anime/catalog/references/age-ratings")
    suspend fun getCatalogAgeRatings(): Either<AppError, List<AgeRatingDto>>

    @GET("anime/catalog/references/genres")
    suspend fun getCatalogGenres(): Either<AppError, List<GenreDto>>

    @GET("anime/catalog/references/production-statuses")
    suspend fun getCatalogProductionStatuses(): Either<AppError, List<ProductionStatusDto>>

    @GET("anime/catalog/references/publish-statuses")
    suspend fun getCatalogPublishStatuses(): Either<AppError, List<PublishStatusDto>>

    @GET("anime/catalog/references/seasons")
    suspend fun getCatalogSeasons(): Either<AppError, List<SeasonDto>>

    @GET("anime/catalog/references/sorting")
    suspend fun getCatalogSortingTypes(): Either<AppError, List<SortingTypeDto>>

    @GET("anime/catalog/references/types")
    suspend fun getCatalogReleaseTypes(): Either<AppError, List<ReleaseTypeDto>>

    @GET("anime/catalog/references/years")
    suspend fun getCatalogYears(): Either<AppError, List<Int>>
}
