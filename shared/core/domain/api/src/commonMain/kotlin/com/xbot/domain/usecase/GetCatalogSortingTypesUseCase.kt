package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.SortingType
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetCatalogSortingTypesUseCase {
    suspend operator fun invoke(): Either<AppError, List<SortingType>>
}
