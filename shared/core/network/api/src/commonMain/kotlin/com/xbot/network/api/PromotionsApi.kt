package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.PromotionDto

interface PromotionsApi {
    suspend fun getPromotions(): Either<NetworkError, List<PromotionDto>>
}
