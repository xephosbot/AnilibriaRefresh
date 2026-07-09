package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.Season
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetCatalogSeasonsUseCase {
    suspend operator fun invoke(): Either<AppError, List<Season>>
}
