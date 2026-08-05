package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.PromotionDto

interface PromotionsApi {
    suspend fun getPromotions(): Either<AppError, List<PromotionDto>>
}
