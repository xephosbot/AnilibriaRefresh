package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.VideoDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultVideosApi(private val client: HttpClient) : VideosApi {
    override suspend fun getVideos(limit: Int): Either<NetworkError, List<VideoDto>> = client.request {
        get("media/videos") {
            parameter("limit", limit)
        }
    }
}
