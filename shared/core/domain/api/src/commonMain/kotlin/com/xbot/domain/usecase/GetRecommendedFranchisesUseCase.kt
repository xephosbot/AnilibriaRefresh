package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Franchise

fun interface GetRecommendedFranchisesUseCase {
    suspend operator fun invoke(): Either<DomainError, List<Franchise>>
}
