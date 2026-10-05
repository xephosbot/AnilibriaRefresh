package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.responses.PaginatedResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface GenresApi {
    @GET("anime/genres")
    suspend fun getGenres(): Either<AppError, List<GenreDto>>

    @GET("anime/genres/{genreId}")
    suspend fun getGenre(@Path("genreId") genreId: Int): Either<AppError, GenreDto>

    @GET("anime/genres/random")
    suspend fun getRandomGenres(@Query("limit") limit: Int): Either<AppError, List<GenreDto>>

    @GET("anime/genres/{genreId}/releases")
    suspend fun getGenreReleases(
        @Path("genreId") genreId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Either<AppError, PaginatedResponse<ReleaseDto>>
}
