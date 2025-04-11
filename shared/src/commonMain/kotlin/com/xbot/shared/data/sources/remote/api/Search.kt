package com.xbot.shared.data.sources.remote.api

import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.models.shared.ReleaseApi
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.search(query: String) = request<List<ReleaseApi>> {
    get("app/search/releases") {
        parameter("query", query)
    }
}