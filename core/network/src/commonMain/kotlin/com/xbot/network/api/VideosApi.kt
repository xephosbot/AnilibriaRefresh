package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.VideoDto
import io.ktor.client.*
import io.ktor.client.request.*
import org.koin.core.annotation.Singleton

interface VideosApi {
    suspend fun getVideos(limit: Int): Either<NetworkError, List<VideoDto>>
}

@Singleton
internal class DefaultVideosApi(private val client: HttpClient) : VideosApi {
    override suspend fun getVideos(limit: Int): Either<NetworkError, List<VideoDto>> = client.request {
        get("media/videos") {
            parameter("limit", limit)
        }
    }
}