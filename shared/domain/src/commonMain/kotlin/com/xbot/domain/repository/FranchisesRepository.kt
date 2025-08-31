package com.xbot.domain.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.domain.models.Franchise

interface FranchisesRepository {
    suspend fun getFranchises(): PagingSource<Int, Franchise>
    suspend fun getFranchise(franchiseId: String): Either<Error, Franchise>
    suspend fun getRandomFranchises(limit: Int): Either<Error, List<Franchise>>
    suspend fun getReleaseFranchises(releaseId: String): Either<Error, List<Franchise>>
}