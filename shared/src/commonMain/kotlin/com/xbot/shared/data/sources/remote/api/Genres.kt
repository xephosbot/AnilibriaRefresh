package com.xbot.shared.data.sources.remote.api

import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.models.shared.GenreApi
import com.xbot.shared.data.sources.remote.models.shared.ReleasesWithMetadataApi
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getGenres() = request<List<GenreApi>> {
    get("anime/genres")
}

suspend fun AnilibriaClient.getGenre(id: Int) = request<GenreApi> {
    get("anime/genres/$id")
}

suspend fun AnilibriaClient.getRandomGenres(limit: Int) = request<List<GenreApi>> {
    get("anime/genres/random") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getGenreReleases(id: Int, page: Int, limit: Int) = request<ReleasesWithMetadataApi> {
    get("anime/genres/$id/releases") {
        parameter("page", page)
        parameter("limit", limit)
    }
}