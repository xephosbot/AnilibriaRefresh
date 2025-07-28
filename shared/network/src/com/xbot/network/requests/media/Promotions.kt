package com.xbot.network.requests.media

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.media.PromotionApi
import io.ktor.client.request.get

suspend fun AnilibriaClient.getPromotions(): List<PromotionApi> = request {
    get("media/promotions")
}