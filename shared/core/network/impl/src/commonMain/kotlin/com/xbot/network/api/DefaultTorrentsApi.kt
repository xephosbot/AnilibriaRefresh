package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.dto.TorrentDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultTorrentsApi(private val requester: ResilientHttpRequester) : TorrentsApi {
    override suspend fun getTorrents(
        page: Int,
        limit: Int
    ): Either<DomainError, PaginatedResponse<TorrentDto>> = requester.request {
        get("anime/torrents") {
            parameter("page", page)
            parameter("limit", limit)
        }
    }

    override suspend fun getTorrent(hashOrId: String): Either<DomainError, TorrentDto> = requester.request {
        get("anime/torrents/$hashOrId")
    }

    override suspend fun getTorrentFile(
        hashOrId: String,
        pk: String?
    ): Either<DomainError, ByteArray> = requester.request {
        get("anime/torrents/$hashOrId/file") {
            pk?.let { parameter("pk", it) }
        }
    }

    override suspend fun getReleaseTorrents(releaseId: Int): Either<DomainError, List<TorrentDto>> = requester.request {
        get("anime/torrents/release/$releaseId")
    }

    override suspend fun getTorrentsRss(
        limit: Int?,
        pk: String?
    ): Either<DomainError, String> = requester.request {
        get("anime/torrents/rss") {
            limit?.let { parameter("limit", it) }
            pk?.let { parameter("pk", it) }
        }
    }

    override suspend fun getReleaseTorrentsRss(
        releaseId: Int,
        pk: String?
    ): Either<DomainError, String> = requester.request {
        get("anime/torrents/rss/release/$releaseId") {
            pk?.let { parameter("pk", it) }
        }
    }
}
