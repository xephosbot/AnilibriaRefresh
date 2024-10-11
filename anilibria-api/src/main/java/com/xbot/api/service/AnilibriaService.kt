package com.xbot.api.service

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.models.Anime
import com.xbot.api.models.AnimeCatalogResponse
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

    companion object {
        const val BASE_URL: String = "https://anilibria.top"
        const val BASE_URL_API: String = "https://anilibria.top/api/v1/"
    }
}