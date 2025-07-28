package com.xbot.network.requests.anime

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.TorrentApi
import com.xbot.network.models.responses.common.PaginatedResponse
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getTorrents(
    page: Int,
    limit: Int
): PaginatedResponse<TorrentApi> = request {
    get("anime/torrents") {
        parameter("page", page)
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getTorrent(
    hashOrId: String
): TorrentApi = request {
    get("anime/torrents/$hashOrId")
}

suspend fun AnilibriaClient.getTorrentFile(
    hashOrId: String,
    pk: String? = null
): ByteArray = request {
    get("anime/torrents/$hashOrId/file") {
        pk?.let { parameter("pk", it) }
    }
}

suspend fun AnilibriaClient.getReleaseTorrents(
    releaseId: Int
): List<TorrentApi> = request {
    get("anime/torrents/release/$releaseId")
}

suspend fun AnilibriaClient.getTorrentsRss(
    limit: Int? = null,
    pk: String? = null
): String = request {
    get("anime/torrents/rss") {
        limit?.let { parameter("limit", it) }
        pk?.let { parameter("pk", it) }
    }
}

suspend fun AnilibriaClient.getReleaseTorrentsRss(
    releaseId: Int,
    pk: String? = null
): String = request {
    get("anime/torrents/rss/release/$releaseId") {
        pk?.let { parameter("pk", it) }
    }
}