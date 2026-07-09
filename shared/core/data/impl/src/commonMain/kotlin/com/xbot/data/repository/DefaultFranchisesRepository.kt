package com.xbot.data.repository

import arrow.core.Either
import arrow.core.raise.either
import com.xbot.data.mapper.toDomain
import com.xbot.common.error.AppError
import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Release
import com.xbot.network.api.FranchisesApi
import com.xbot.network.api.ReleasesApi
import com.xbot.network.models.dto.FranchiseDto
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultFranchisesRepository(
    private val releasesApi: ReleasesApi,
    private val franchisesApi: FranchisesApi
) : FranchisesRepository {
    override suspend fun getFranchises(): Either<AppError, List<Franchise>> = franchisesApi
        .getFranchises()
        .map { it.map(FranchiseDto::toDomain) }

    override suspend fun getFranchise(franchiseId: String): Either<AppError, Franchise> = franchisesApi
        .getFranchise(franchiseId)
        .map(FranchiseDto::toDomain)

    override suspend fun getRandomFranchises(limit: Int): Either<AppError, List<Franchise>> = franchisesApi
        .getFranchisesRandom(limit)
        .map { it.map(FranchiseDto::toDomain) }

    override suspend fun getReleaseFranchises(releaseId: Int): Either<AppError, List<Franchise>> = franchisesApi
        .getFranchisesByRelease(releaseId)
        .map { it.map(FranchiseDto::toDomain) }

    override suspend fun getFranchiseReleases(aliasOrId: String): Either<AppError, List<Release>> = either {
        val releaseId = aliasOrId.toIntOrNull() ?: releasesApi.getRelease(aliasOrId)
            .bind()
            .id

        val franchises = franchisesApi.getFranchisesByRelease(releaseId)
            .bind()

        franchises
            .flatMap { it.franchiseReleases ?: emptyList() }
            .map { it.release.toDomain() }
            .filterNot { it.id == releaseId }
    }
}
