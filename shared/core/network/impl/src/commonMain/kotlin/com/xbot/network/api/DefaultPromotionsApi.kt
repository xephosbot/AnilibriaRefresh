package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.PromotionDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultPromotionsApi(private val client: HttpClient) : PromotionsApi {
    override suspend fun getPromotions(): Either<NetworkError, List<PromotionDto>> = client.request {
        get("media/promotions")
    }
}
