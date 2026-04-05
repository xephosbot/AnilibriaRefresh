package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.VastDto

interface AdsApi {
    suspend fun getVasts(): Either<NetworkError, List<VastDto>>
    suspend fun getVastsChain(): Either<NetworkError, String>
}
