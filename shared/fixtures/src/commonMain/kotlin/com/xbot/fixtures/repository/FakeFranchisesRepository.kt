package com.xbot.fixtures.repository

import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Release
import com.xbot.domain.repository.FranchisesRepository
import com.xbot.fixtures.data.franchiseMocks
import org.koin.core.annotation.Singleton

@Singleton
class FakeFranchisesRepository : FranchisesRepository {
    override suspend fun getFranchises(): Either<DomainError, List<Franchise>> {
        return franchiseMocks.right()
    }

    override suspend fun getFranchise(franchiseId: String): Either<DomainError, Franchise> {
        val franchise = franchiseMocks.find { it.id == franchiseId } ?: franchiseMocks.first()
        return franchise.right()
    }

    override suspend fun getRandomFranchises(limit: Int): Either<DomainError, List<Franchise>> {
        return franchiseMocks.shuffled().take(limit).right()
    }

    override suspend fun getReleaseFranchises(releaseId: Int): Either<DomainError, List<Franchise>> {
        return franchiseMocks.right()
    }

    override suspend fun getFranchiseReleases(aliasOrId: String): Either<DomainError, List<Release>> {
        val releaseId = aliasOrId.toIntOrNull()
        val franchise = franchiseMocks.find { franchise ->
            franchise.franchiseReleases?.any { it.id == releaseId } == true
        } ?: franchiseMocks.first()

        return franchise.franchiseReleases
            ?.filter { releaseId == null || it.id != releaseId }
            .orEmpty()
            .right()
    }
}
