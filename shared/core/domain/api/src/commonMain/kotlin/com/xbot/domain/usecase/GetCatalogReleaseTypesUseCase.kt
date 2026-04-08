package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.ReleaseType

fun interface GetCatalogReleaseTypesUseCase {
    suspend operator fun invoke(): Either<DomainError, List<ReleaseType>>
}
