package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.fx.coroutines.parMap
import com.xbot.data.repository.FranchisesRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Franchise
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetRecommendedFranchisesUseCase(
    private val franchisesRepository: FranchisesRepository
) : GetRecommendedFranchisesUseCase {
    override suspend fun invoke(): Either<DomainError, List<Franchise>> = either {
        val franchises = franchisesRepository.getRandomFranchises(10).bind()
        franchises.parMap { franchise ->
            franchisesRepository.getFranchise(franchise.id).getOrElse { franchise }
        }
    }
}
