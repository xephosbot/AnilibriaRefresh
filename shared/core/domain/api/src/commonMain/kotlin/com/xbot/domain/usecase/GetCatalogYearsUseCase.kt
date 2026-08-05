package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetCatalogYearsUseCase {
    suspend operator fun invoke(): Either<AppError, IntRange>
}
