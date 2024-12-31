package com.xbot.api.request

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.franchises.FranchiseApi
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