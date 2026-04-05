package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.VideoDto

interface VideosApi {
    suspend fun getVideos(limit: Int): Either<NetworkError, List<VideoDto>>
}
