package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.FranchiseDto

interface FranchisesApi {
    suspend fun getFranchises(): Either<AppError, List<FranchiseDto>>
    suspend fun getFranchise(franchiseId: String): Either<AppError, FranchiseDto>
    suspend fun getFranchisesRandom(limit: Int): Either<AppError, List<FranchiseDto>>
    suspend fun getFranchisesByRelease(releaseId: Int): Either<AppError, List<FranchiseDto>>
}
