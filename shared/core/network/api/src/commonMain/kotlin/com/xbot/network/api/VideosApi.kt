package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.VideoDto

interface VideosApi {
    suspend fun getVideos(limit: Int): Either<DomainError, List<VideoDto>>
}
