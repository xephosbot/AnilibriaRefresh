package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.VastDto

interface AdsApi {
    suspend fun getVasts(): Either<AppError, List<VastDto>>
    suspend fun getVastsChain(): Either<AppError, String>
}
