package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.responses.PaginatedResponse

interface GenresApi {
    suspend fun getGenres(): Either<DomainError, List<GenreDto>>
    suspend fun getGenre(genreId: Int): Either<DomainError, GenreDto>
    suspend fun getRandomGenres(limit: Int): Either<DomainError, List<GenreDto>>
    suspend fun getGenreReleases(
        genreId: Int,
        page: Int,
        limit: Int
    ): Either<DomainError, PaginatedResponse<ReleaseDto>>
}
