package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.SortingType

fun interface GetCatalogSortingTypesUseCase {
    suspend operator fun invoke(): Either<DomainError, List<SortingType>>
}
