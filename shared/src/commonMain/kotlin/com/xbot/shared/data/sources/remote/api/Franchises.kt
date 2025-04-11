package com.xbot.shared.data.sources.remote.api

import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.models.franchises.FranchiseApi
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getFranchises() = request<List<FranchiseApi>> {
    get("anime/franchises")
}

suspend fun AnilibriaClient.getFranchise(id: Int) = request<FranchiseApi> {
    get("anime/franchises/${id}")
}

suspend fun AnilibriaClient.getFranchisesRandom(limit: Int) = request<List<FranchiseApi>> {
    get("anime/franchises/random") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getFranchisesByRelease(releaseId: Int) = request<List<FranchiseApi>> {
    get("anime/franchises/release/${releaseId}")
}