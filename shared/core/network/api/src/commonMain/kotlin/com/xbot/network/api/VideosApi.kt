package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.VideoDto

interface VideosApi {
    suspend fun getVideos(limit: Int): Either<AppError, List<VideoDto>>
}
