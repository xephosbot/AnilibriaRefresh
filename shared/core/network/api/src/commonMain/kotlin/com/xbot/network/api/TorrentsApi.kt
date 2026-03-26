package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.TorrentDto
import com.xbot.network.models.responses.PaginatedResponse

interface TorrentsApi {
    suspend fun getTorrents(page: Int, limit: Int): Either<NetworkError, PaginatedResponse<TorrentDto>>
    suspend fun getTorrent(hashOrId: String): Either<NetworkError, TorrentDto>
    suspend fun getTorrentFile(hashOrId: String, pk: String? = null): Either<NetworkError, ByteArray>
    suspend fun getReleaseTorrents(releaseId: Int): Either<NetworkError, List<TorrentDto>>
    suspend fun getTorrentsRss(limit: Int? = null, pk: String? = null): Either<NetworkError, String>
    suspend fun getReleaseTorrentsRss(releaseId: Int, pk: String? = null): Either<NetworkError, String>
}
