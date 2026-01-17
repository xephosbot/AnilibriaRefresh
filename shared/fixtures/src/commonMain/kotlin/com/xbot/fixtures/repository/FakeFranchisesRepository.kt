package com.xbot.fixtures.repository

import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Franchise
import com.xbot.domain.repository.FranchisesRepository
import com.xbot.fixtures.data.franchiseMocks

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
}
