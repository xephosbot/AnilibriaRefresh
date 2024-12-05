package com.xbot.api.catalog

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.genres.models.Genre
import com.xbot.api.shared.ReleaseCatalogResponse
import com.xbot.api.shared.ValDesc
import com.xbot.api.shared.enums.AgeRatingEnum
import com.xbot.api.shared.enums.ProductionStatusEnum
import com.xbot.api.shared.enums.PublishStatusEnum
import com.xbot.api.shared.enums.ReleaseTypeEnum
import com.xbot.api.shared.enums.SeasonEnum
import com.xbot.api.shared.enums.SortingTypeEnum
import retrofit2.http.GET
import retrofit2.http.Query

interface CatalogService {
    @GET("anime/catalog/references/age-ratings")
    suspend fun getAgeRatings(): ApiResponse<List<ValDesc<AgeRatingEnum>>>

    @GET("anime/catalog/references/genres")
    suspend fun getGenres(): ApiResponse<List<Genre>>

    @GET("anime/catalog/references/production-statuses")
    suspend fun getProductionStatuses(): ApiResponse<List<ValDesc<ProductionStatusEnum>>>

    @GET("anime/catalog/references/publish-statuses")
    suspend fun getPublishStatuses(): ApiResponse<List<ValDesc<PublishStatusEnum>>>

    @GET("anime/catalog/references/seasons")
    suspend fun getSeasons(): ApiResponse<List<ValDesc<SeasonEnum>>>

    @GET("anime/catalog/references/sorting")
    suspend fun getSortingTypes(): ApiResponse<List<ValDesc<SortingTypeEnum>>>

    @GET("anime/catalog/references/types")
    suspend fun getTypeReleases(): ApiResponse<List<ValDesc<ReleaseTypeEnum>>>

    @GET("anime/catalog/references/years")
    suspend fun getYears(): ApiResponse<List<Int>>

    @GET("anime/catalog/releases")
    suspend fun getReleases(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("f[genres]") genres: String? = null,
        @Query("f[types]") types: String? = null,
        @Query("f[seasons]") seasons: String? = null,
        @Query("f[years][from_year]") fromYear: Int? = null,
        @Query("f[years][to_year]") toYear: Int? = null,
        @Query("f[search]") search: String? = null,
        @Query("f[sorting]") sorting: String? = null,
        @Query("f[age_ratings]") ageRatings: String? = null,
        @Query("f[publish_statuses]") publishStatuses: String? = null,
        @Query("f[production_statuses]") productionStatuses: String? = null,
    ): ApiResponse<ReleaseCatalogResponse>
}