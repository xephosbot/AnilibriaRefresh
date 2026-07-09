package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Release
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetRecommendedReleasesUseCase {
    suspend operator fun invoke(): Either<AppError, List<Release>>
}
