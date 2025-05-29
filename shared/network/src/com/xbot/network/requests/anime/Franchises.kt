package com.xbot.network.requests.anime

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.FranchiseApi
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getFranchises(): List<FranchiseApi> = request {
    get("anime/franchises")
}

suspend fun AnilibriaClient.getFranchise(
    franchiseId: Int
): FranchiseApi = request {
    get("anime/franchises/${franchiseId}")
}

suspend fun AnilibriaClient.getFranchisesRandom(
    limit: Int
): List<FranchiseApi> = request {
    get("anime/franchises/random") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getFranchisesByRelease(
    releaseId: Int
): List<FranchiseApi> = request {
    get("anime/franchises/release/${releaseId}")
}