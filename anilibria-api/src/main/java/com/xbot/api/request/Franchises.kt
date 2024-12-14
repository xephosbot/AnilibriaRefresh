package com.xbot.api.request

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.franchises.Franchise
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getFranchises(): List<Franchise> = request {
    get("anime/franchises")
}

suspend fun AnilibriaClient.getFranchise(id: Int): Franchise = request {
    get("anime/franchises/${id}")
}

suspend fun AnilibriaClient.getFranchisesRandom(limit: Int): List<Franchise> = request {
    get("anime/franchises/random") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getFranchisesByRelease(releaseId: Int): List<Franchise> = request {
    get("anime/franchises/release/${releaseId}")
}