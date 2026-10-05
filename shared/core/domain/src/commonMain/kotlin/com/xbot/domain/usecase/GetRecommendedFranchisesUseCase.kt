package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.fx.coroutines.parMap
import com.xbot.common.error.AppError
import com.xbot.domain.models.Franchise
import com.xbot.domain.repository.FranchisesRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetRecommendedFranchisesUseCase(private val franchisesRepository: FranchisesRepository) :
    EitherUseCase<Unit, List<Franchise>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<Franchise>> = either {
        val franchises = franchisesRepository.getRandomFranchises(10).bind()
        franchises.parMap { franchise ->
            franchisesRepository.getFranchise(franchise.id).getOrElse { franchise }
        }
    }
}
