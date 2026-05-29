package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetCatalogGenresUseCase {
    suspend operator fun invoke(): Either<DomainError, List<Genre>>
}
