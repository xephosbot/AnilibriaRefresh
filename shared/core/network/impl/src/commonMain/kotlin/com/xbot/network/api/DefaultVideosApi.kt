package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.models.dto.VideoDto
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultVideosApi(private val requester: HttpRequester) : VideosApi {
    override suspend fun getVideos(limit: Int): Either<AppError, List<VideoDto>> = requester.request {
        get("media/videos") {
            parameter("limit", limit)
        }
    }
}
