package com.xbot.network.requests.ads

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.ads.VastApi
import io.ktor.client.request.get

suspend fun AnilibriaClient.getVasts(): List<VastApi> = request {
    get("ads/vasts")
}

suspend fun AnilibriaClient.getVastsChain(): String = request {
    get("ads/vasts/chain")
}