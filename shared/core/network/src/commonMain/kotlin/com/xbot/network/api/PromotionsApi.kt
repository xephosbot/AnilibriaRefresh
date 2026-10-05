package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.PromotionDto
import de.jensklingenberg.ktorfit.http.GET

interface PromotionsApi {
    @GET("media/promotions")
    suspend fun getPromotions(): Either<AppError, List<PromotionDto>>
}
