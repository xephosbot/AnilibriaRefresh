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
