package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.FranchiseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface FranchisesApi {
    suspend fun getFranchises(): Either<NetworkError, List<FranchiseDto>>
    suspend fun getFranchise(franchiseId: Int): Either<NetworkError, FranchiseDto>
    suspend fun getFranchisesRandom(limit: Int): Either<NetworkError, List<FranchiseDto>>
    suspend fun getFranchisesByRelease(releaseId: Int): Either<NetworkError, List<FranchiseDto>>
}

internal class DefaultFranchisesApi(private val client: HttpClient) : FranchisesApi {
    override suspend fun getFranchises(): Either<NetworkError, List<FranchiseDto>> = client.request {
        get("anime/franchises")
    }

    override suspend fun getFranchise(franchiseId: Int): Either<NetworkError, FranchiseDto> = client.request {
        get("anime/franchises/${franchiseId}")
    }

    override suspend fun getFranchisesRandom(limit: Int): Either<NetworkError, List<FranchiseDto>> = client.request {
        get("anime/franchises/random") {
            parameter("limit", limit)
        }
    }

    override suspend fun getFranchisesByRelease(releaseId: Int): Either<NetworkError, List<FranchiseDto>> = client.request {
        get("anime/franchises/release/${releaseId}")
    }

}