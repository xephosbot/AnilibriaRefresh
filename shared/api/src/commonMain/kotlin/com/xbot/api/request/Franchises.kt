package com.xbot.api.request

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.franchises.Franchise
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getFranchises() = request<List<Franchise>> {
    get("anime/franchises")
}

suspend fun AnilibriaClient.getFranchise(id: Int) = request<Franchise> {
    get("anime/franchises/${id}")
}

suspend fun AnilibriaClient.getFranchisesRandom(limit: Int) = request<List<Franchise>> {
    get("anime/franchises/random") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getFranchisesByRelease(releaseId: Int) = request<List<Franchise>> {
    get("anime/franchises/release/${releaseId}")
}