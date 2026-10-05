package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.TorrentDto
import com.xbot.network.models.responses.PaginatedResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface TorrentsApi {
    @GET("anime/torrents")
    suspend fun getTorrents(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Either<AppError, PaginatedResponse<TorrentDto>>

    @GET("anime/torrents/{hashOrId}")
    suspend fun getTorrent(@Path("hashOrId") hashOrId: String): Either<AppError, TorrentDto>

    @GET("anime/torrents/{hashOrId}/file")
    suspend fun getTorrentFile(
        @Path("hashOrId") hashOrId: String,
        @Query("pk") pk: String? = null
    ): Either<AppError, ByteArray>

    @GET("anime/torrents/release/{releaseId}")
    suspend fun getReleaseTorrents(
        @Path("releaseId") releaseId: Int
    ): Either<AppError, List<TorrentDto>>

    @GET("anime/torrents/rss")
    suspend fun getTorrentsRss(
        @Query("limit") limit: Int? = null,
        @Query("pk") pk: String? = null
    ): Either<AppError, String>

    @GET("anime/torrents/rss/release/{releaseId}")
    suspend fun getReleaseTorrentsRss(
        @Path("releaseId") releaseId: Int,
        @Query("pk") pk: String? = null
    ): Either<AppError, String>
}
