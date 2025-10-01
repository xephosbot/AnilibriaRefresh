package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Franchise

interface FranchisesRepository {
    suspend fun getFranchises(): Either<DomainError, List<Franchise>>
    suspend fun getFranchise(franchiseId: String): Either<DomainError, Franchise>
    suspend fun getRandomFranchises(limit: Int): Either<DomainError, List<Franchise>>
    suspend fun getReleaseFranchises(releaseId: Int): Either<DomainError, List<Franchise>>
}