package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.ReleaseDto

interface SearchApi {
    suspend fun searchReleases(query: String): Either<NetworkError, List<ReleaseDto>>
}
