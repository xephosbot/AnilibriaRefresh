package com.xbot.data.repository

import arrow.core.Either
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Error
import com.xbot.domain.models.Franchise
import com.xbot.domain.repository.FranchisesRepository
import com.xbot.network.api.FranchisesApi
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.FranchiseDto

internal class DefaultFranchisesRepository(
    private val franchisesApi: FranchisesApi
) : FranchisesRepository {
    override suspend fun getFranchises(): Either<Error, List<Franchise>> = franchisesApi
        .getFranchises()
        .mapLeft(NetworkError::toDomain)
        .map { it.map(FranchiseDto::toDomain) }

    override suspend fun getFranchise(franchiseId: String): Either<Error, Franchise> = franchisesApi
        .getFranchise(franchiseId)
        .mapLeft(NetworkError::toDomain)
        .map(FranchiseDto::toDomain)

    override suspend fun getRandomFranchises(limit: Int): Either<Error, List<Franchise>> = franchisesApi
        .getFranchisesRandom(limit)
        .mapLeft(NetworkError::toDomain)
        .map { it.map(FranchiseDto::toDomain) }

    override suspend fun getReleaseFranchises(releaseId: Int): Either<Error, List<Franchise>> = franchisesApi
        .getFranchisesByRelease(releaseId)
        .mapLeft(NetworkError::toDomain)
        .map { it.map(FranchiseDto::toDomain) }
}