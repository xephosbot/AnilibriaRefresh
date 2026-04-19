package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.PromotionDto

interface PromotionsApi {
    suspend fun getPromotions(): Either<DomainError, List<PromotionDto>>
}
