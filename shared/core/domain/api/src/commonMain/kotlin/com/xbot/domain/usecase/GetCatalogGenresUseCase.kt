package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Genre
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetCatalogGenresUseCase {
    suspend operator fun invoke(): Either<AppError, List<Genre>>
}
