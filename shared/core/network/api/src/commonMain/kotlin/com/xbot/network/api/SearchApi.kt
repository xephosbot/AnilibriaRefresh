package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.ReleaseDto

interface SearchApi {
    suspend fun searchReleases(query: String): Either<AppError, List<ReleaseDto>>
}
