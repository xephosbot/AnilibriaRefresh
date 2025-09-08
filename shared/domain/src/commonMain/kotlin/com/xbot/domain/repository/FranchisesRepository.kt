package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.Error
import com.xbot.domain.models.Franchise

interface FranchisesRepository {
    suspend fun getFranchises(): Either<Error, List<Franchise>>
    suspend fun getFranchise(franchiseId: String): Either<Error, Franchise>
    suspend fun getRandomFranchises(limit: Int): Either<Error, List<Franchise>>
    suspend fun getReleaseFranchises(releaseId: Int): Either<Error, List<Franchise>>
}