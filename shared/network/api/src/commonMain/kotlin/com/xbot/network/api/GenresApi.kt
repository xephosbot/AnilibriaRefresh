package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.responses.PaginatedResponse

interface GenresApi {
    suspend fun getGenres(): Either<NetworkError, List<GenreDto>>
    suspend fun getGenre(genreId: Int): Either<NetworkError, GenreDto>
    suspend fun getRandomGenres(limit: Int): Either<NetworkError, List<GenreDto>>
    suspend fun getGenreReleases(
        genreId: Int,
        page: Int,
        limit: Int
    ): Either<NetworkError, PaginatedResponse<ReleaseDto>>
}
