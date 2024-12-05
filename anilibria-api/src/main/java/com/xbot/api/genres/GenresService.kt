package com.xbot.api.genres

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.genres.models.Genre
import com.xbot.api.shared.ReleaseCatalogResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GenresService {
    @GET("anime/genres")
    suspend fun getAllGenres(): ApiResponse<List<Genre>>

    @GET("anime/genres/{genreId}")
    suspend fun getGenreById(
        @Path("genreId") id: Int,
    ): ApiResponse<Genre>

    @GET("anime/genres/random")
    suspend fun getRandomGenres(
        @Query("limit") limit: Int,
    ): ApiResponse<List<Genre>>

    @GET("anime/genres/{genreId}/releases")
    suspend fun getAllReleasesByGenre(
        @Path("genreId") id: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): ApiResponse<ReleaseCatalogResponse>
}