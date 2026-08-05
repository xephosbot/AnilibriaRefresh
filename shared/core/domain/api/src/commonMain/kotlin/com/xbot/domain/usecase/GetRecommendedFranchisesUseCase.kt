package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Franchise
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetRecommendedFranchisesUseCase {
    suspend operator fun invoke(): Either<AppError, List<Franchise>>
}
