package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultGenresApi(private val requester: ResilientHttpRequester) : GenresApi {
    override suspend fun getGenres(): Either<DomainError, List<GenreDto>> = requester.request {
        get("anime/genres")
    }

    override suspend fun getGenre(genreId: Int): Either<DomainError, GenreDto> = requester.request {
        get("anime/genres/$genreId")
    }

    override suspend fun getRandomGenres(limit: Int): Either<DomainError, List<GenreDto>> = requester.request {
        get("anime/genres/random") {
            parameter("limit", limit)
        }
    }

    override suspend fun getGenreReleases(
        genreId: Int,
        page: Int,
        limit: Int
    ): Either<DomainError, PaginatedResponse<ReleaseDto>> = requester.request {
        get("anime/genres/$genreId/releases") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }
}
