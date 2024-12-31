package com.xbot.api.request

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.shared.ReleaseApi
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.search(query: String) = request<List<ReleaseApi>> {
    get("app/search/releases") {
        parameter("query", query)
    }
}