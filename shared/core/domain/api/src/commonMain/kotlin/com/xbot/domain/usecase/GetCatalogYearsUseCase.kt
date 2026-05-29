package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetCatalogYearsUseCase {
    suspend operator fun invoke(): Either<DomainError, IntRange>
}
