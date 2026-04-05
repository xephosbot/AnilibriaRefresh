package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.client.requiresAuth
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.xbot.network.models.dto.*
import com.xbot.network.models.enums.*
import com.xbot.network.models.responses.*
import org.koin.core.annotation.Singleton

@Singleton
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
