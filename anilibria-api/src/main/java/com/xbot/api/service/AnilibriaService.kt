package com.xbot.api.service

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.models.AgeRating
import com.xbot.api.models.Anime
import com.xbot.api.models.AnimeCatalogResponse
import com.xbot.api.models.Franchise
import com.xbot.api.models.Genre
import com.xbot.api.models.ProductionStatus
import com.xbot.api.models.PublishStatus
import com.xbot.api.models.Season
import com.xbot.api.models.SortingType
import com.xbot.api.models.Type
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Интерфейс для взаимодействия с API Anilibria для получения информации о релизах аниме.
 * @see <a href="https://anilibria.top/api/docs/v1">Документация API Anilibria</a>
 */
interface AnilibriaService {
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
        @Query("f[production_statuses]") productionStatuses: String? = null
    ): ApiResponse<AnimeCatalogResponse>

    @GET("anime/releases/{id}")
    suspend fun getRelease(
        @Path("id") id: Int
    ): ApiResponse<Anime>

    @GET("anime/catalog/references/age-ratings")
    suspend fun getAgeRatings(): ApiResponse<List<AgeRating>>

    @GET("anime/catalog/references/genres")
    suspend fun getGenres(): ApiResponse<List<Genre>>

    @GET("anime/catalog/references/production-statuses")
    suspend fun getProductionStatuses(): ApiResponse<List<ProductionStatus>>

    @GET("anime/catalog/references/publish-statuses")
    suspend fun getPublishStatuses(): ApiResponse<List<PublishStatus>>

    @GET("anime/catalog/references/seasons")
    suspend fun getSeasons(): ApiResponse<List<Season>>

    @GET("anime/catalog/references/sorting")
    suspend fun getSortingTypes(): ApiResponse<List<SortingType>>

    @GET("anime/catalog/references/types")
    suspend fun getTypeReleases(): ApiResponse<List<Type>>

    @GET("anime/catalog/references/years")
    suspend fun getYears(): ApiResponse<List<Int>>

    @GET("anime/franchises")
    suspend fun getFranchises(): ApiResponse<List<Franchise>>

    @GET("anime/franchises/{franchiseId}")
    suspend fun getFranchiseById(
        @Path("franchiseId") id: Int
    ): ApiResponse<Franchise>

    @GET("anime/franchises/random")
    suspend fun getFranchisesRandom(
        @Query("limit") limit: Int
    ): ApiResponse<List<Franchise>>

    @GET("anime/franchises/release/{releaseId}")
    suspend fun getFranchisesByRelease(
        @Path("releaseId") id: Int
    ): ApiResponse<List<Franchise>>

    @GET("anime/genres")
    suspend fun getAllGenres(): ApiResponse<List<Genre>>

    @GET("anime/genres/{genreId}")
    suspend fun getGenreById(
        @Path("genreId") id: Int
    ): ApiResponse<Genre>

    @GET("anime/genres/random")
    suspend fun getRandomGenres(
        @Query("limit") limit: Int
    ): ApiResponse<List<Genre>>

    @GET("anime/genres/{genreId}/releases")
    suspend fun getAllReleasesByGenre(
        @Path("genreId") id: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ApiResponse<AnimeCatalogResponse>

    companion object {
        const val BASE_URL: String = "https://anilibria.top"
        const val BASE_URL_API: String = "https://anilibria.top/api/v1/"
    }
}