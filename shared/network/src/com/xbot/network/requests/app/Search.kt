package com.xbot.network.requests.app

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.ReleaseApi
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.searchReleases(
    query: String
): List<ReleaseApi> = request {
    get("app/search/releases") {
        parameter("query", query)
    }
}