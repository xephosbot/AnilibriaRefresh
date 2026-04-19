package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.dto.PromotionDto
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultPromotionsApi(private val requester: ResilientHttpRequester) : PromotionsApi {
    override suspend fun getPromotions(): Either<DomainError, List<PromotionDto>> = requester.request {
        get("media/promotions")
    }
}
