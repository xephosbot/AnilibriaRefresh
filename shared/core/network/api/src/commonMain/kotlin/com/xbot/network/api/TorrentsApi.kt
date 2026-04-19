package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.TorrentDto
import com.xbot.network.models.responses.PaginatedResponse

interface TorrentsApi {
    suspend fun getTorrents(page: Int, limit: Int): Either<DomainError, PaginatedResponse<TorrentDto>>
    suspend fun getTorrent(hashOrId: String): Either<DomainError, TorrentDto>
    suspend fun getTorrentFile(hashOrId: String, pk: String? = null): Either<DomainError, ByteArray>
    suspend fun getReleaseTorrents(releaseId: Int): Either<DomainError, List<TorrentDto>>
    suspend fun getTorrentsRss(limit: Int? = null, pk: String? = null): Either<DomainError, String>
    suspend fun getReleaseTorrentsRss(releaseId: Int, pk: String? = null): Either<DomainError, String>
}
