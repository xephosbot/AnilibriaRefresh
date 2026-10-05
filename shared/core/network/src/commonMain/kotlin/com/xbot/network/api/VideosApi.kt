package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.VideoDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface VideosApi {
    @GET("media/videos")
    suspend fun getVideos(@Query("limit") limit: Int): Either<AppError, List<VideoDto>>
}
