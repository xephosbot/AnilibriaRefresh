package com.xbot.data.repository

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Release

interface FranchisesRepository {
    suspend fun getFranchises(): Either<AppError, List<Franchise>>
    suspend fun getFranchise(franchiseId: String): Either<AppError, Franchise>
    suspend fun getRandomFranchises(limit: Int): Either<AppError, List<Franchise>>
    suspend fun getReleaseFranchises(releaseId: Int): Either<AppError, List<Franchise>>
    suspend fun getFranchiseReleases(aliasOrId: String): Either<AppError, List<Release>>
}