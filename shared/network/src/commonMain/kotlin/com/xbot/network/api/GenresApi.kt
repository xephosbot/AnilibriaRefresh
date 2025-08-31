package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

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

internal class DefaultGenresApi(private val client: HttpClient) : GenresApi {
    override suspend fun getGenres(): Either<NetworkError, List<GenreDto>> = client.request {
        get("anime/genres")
    }

    override suspend fun getGenre(genreId: Int): Either<NetworkError, GenreDto> = client.request {
        get("anime/genres/$genreId")
    }

    override suspend fun getRandomGenres(limit: Int): Either<NetworkError, List<GenreDto>> = client.request {
        get("anime/genres/random") {
            parameter("limit", limit)
        }
    }

    override suspend fun getGenreReleases(
        genreId: Int,
        page: Int,
        limit: Int
    ): Either<NetworkError, PaginatedResponse<ReleaseDto>> = client.request {
        get("anime/genres/$genreId/releases") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }
}