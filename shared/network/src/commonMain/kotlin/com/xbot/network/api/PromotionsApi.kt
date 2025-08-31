package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.PromotionDto
import io.ktor.client.*
import io.ktor.client.request.*

interface PromotionsApi {
    suspend fun getPromotions(): Either<NetworkError, List<PromotionDto>>
}

internal class DefaultPromotionsApi(private val client: HttpClient) : PromotionsApi {
    override suspend fun getPromotions(): Either<NetworkError, List<PromotionDto>> = client.request {
        get("media/promotions")
    }
}