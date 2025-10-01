package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.VastDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

interface AdsApi {
    suspend fun getVasts(): Either<NetworkError, List<VastDto>>
    suspend fun getVastsChain(): Either<NetworkError, String>
}

internal class DefaultAdsApi(private val client: HttpClient) : AdsApi {
    override suspend fun getVasts(): Either<NetworkError, List<VastDto>> = client.request {
        get("ads/vasts")
    }

    override suspend fun getVastsChain(): Either<NetworkError, String> = client.request {
        get("ads/vasts/chain")
    }
}
