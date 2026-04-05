package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.FranchiseDto

interface FranchisesApi {
    suspend fun getFranchises(): Either<NetworkError, List<FranchiseDto>>
    suspend fun getFranchise(franchiseId: String): Either<NetworkError, FranchiseDto>
    suspend fun getFranchisesRandom(limit: Int): Either<NetworkError, List<FranchiseDto>>
    suspend fun getFranchisesByRelease(releaseId: Int): Either<NetworkError, List<FranchiseDto>>
}
