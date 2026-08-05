package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.fx.coroutines.parMap
import com.xbot.data.repository.FranchisesRepository
import com.xbot.common.error.AppError
import com.xbot.domain.models.Franchise
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultGetRecommendedFranchisesUseCase(
    private val franchisesRepository: FranchisesRepository
) : GetRecommendedFranchisesUseCase {
    override suspend fun invoke(): Either<AppError, List<Franchise>> = either {
        val franchises = franchisesRepository.getRandomFranchises(10).bind()
        franchises.parMap { franchise ->
            franchisesRepository.getFranchise(franchise.id).getOrElse { franchise }
        }
    }
}
