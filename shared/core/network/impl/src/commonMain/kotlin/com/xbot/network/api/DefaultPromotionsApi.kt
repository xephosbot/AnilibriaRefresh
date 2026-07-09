package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.models.dto.PromotionDto
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultPromotionsApi(private val requester: HttpRequester) : PromotionsApi {
    override suspend fun getPromotions(): Either<AppError, List<PromotionDto>> = requester.request {
        get("media/promotions")
    }
}
