package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.responses.PaginatedResponse

interface GenresApi {
    suspend fun getGenres(): Either<AppError, List<GenreDto>>
    suspend fun getGenre(genreId: Int): Either<AppError, GenreDto>
    suspend fun getRandomGenres(limit: Int): Either<AppError, List<GenreDto>>
    suspend fun getGenreReleases(
        genreId: Int,
        page: Int,
        limit: Int
    ): Either<AppError, PaginatedResponse<ReleaseDto>>
}
