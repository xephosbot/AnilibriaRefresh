package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.FranchiseDto

interface FranchisesApi {
    suspend fun getFranchises(): Either<DomainError, List<FranchiseDto>>
    suspend fun getFranchise(franchiseId: String): Either<DomainError, FranchiseDto>
    suspend fun getFranchisesRandom(limit: Int): Either<DomainError, List<FranchiseDto>>
    suspend fun getFranchisesByRelease(releaseId: Int): Either<DomainError, List<FranchiseDto>>
}
