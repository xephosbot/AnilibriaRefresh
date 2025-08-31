package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.TorrentDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface TorrentsApi {
    suspend fun getTorrents(page: Int, limit: Int): Either<NetworkError, PaginatedResponse<TorrentDto>>
    suspend fun getTorrent(hashOrId: String): Either<NetworkError, TorrentDto>
    suspend fun getTorrentFile(hashOrId: String, pk: String? = null): Either<NetworkError, ByteArray>
    suspend fun getReleaseTorrents(releaseId: Int): Either<NetworkError, List<TorrentDto>>
    suspend fun getTorrentsRss(limit: Int? = null, pk: String? = null): Either<NetworkError, String>
    suspend fun getReleaseTorrentsRss(releaseId: Int, pk: String? = null): Either<NetworkError, String>
}

internal class DefaultTorrentsApi(private val client: HttpClient) : TorrentsApi {
    override suspend fun getTorrents(
        page: Int,
        limit: Int
    ): Either<NetworkError, PaginatedResponse<TorrentDto>> = client.request {
        get("anime/torrents") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getTorrent(hashOrId: String): Either<NetworkError, TorrentDto> = client.request {
        get("anime/torrents/$hashOrId")
    }

    override suspend fun getTorrentFile(
        hashOrId: String,
        pk: String?
    ): Either<NetworkError, ByteArray> = client.request {
        get("anime/torrents/$hashOrId/file") {
            pk?.let { parameter("pk", it) }
        }
    }

    override suspend fun getReleaseTorrents(releaseId: Int): Either<NetworkError, List<TorrentDto>> = client.request {
        get("anime/torrents/release/$releaseId")
    }

    override suspend fun getTorrentsRss(
        limit: Int?,
        pk: String?
    ): Either<NetworkError, String> = client.request {
        get("anime/torrents/rss") {
            limit?.let { parameter("limit", it) }
            pk?.let { parameter("pk", it) }
        }
    }

    override suspend fun getReleaseTorrentsRss(
        releaseId: Int,
        pk: String?
    ): Either<NetworkError, String> = client.request {
        get("anime/torrents/rss/release/$releaseId") {
            pk?.let { parameter("pk", it) }
        }
    }
}