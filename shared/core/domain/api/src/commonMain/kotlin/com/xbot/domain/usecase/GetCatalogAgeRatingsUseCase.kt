package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.AgeRating
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetCatalogAgeRatingsUseCase {
    suspend operator fun invoke(): Either<AppError, List<AgeRating>>
}
