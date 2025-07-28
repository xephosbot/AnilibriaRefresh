package com.xbot.network.requests.anime

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.ReleaseApi
import com.xbot.network.models.responses.common.PaginatedResponse
import com.xbot.network.models.entities.anime.GenreApi
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getGenres(): List<GenreApi> = request {
    get("anime/genres")
}

suspend fun AnilibriaClient.getGenre(
    genreId: Int
): GenreApi = request {
    get("anime/genres/$genreId")
}

suspend fun AnilibriaClient.getRandomGenres(
    limit: Int
): List<GenreApi> = request {
    get("anime/genres/random") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getGenreReleases(
    genreId: Int,
    page: Int,
    limit: Int
): PaginatedResponse<ReleaseApi> = request {
    get("anime/genres/$genreId/releases") {
        parameter("page", page)
        parameter("limit", limit)
    }
}