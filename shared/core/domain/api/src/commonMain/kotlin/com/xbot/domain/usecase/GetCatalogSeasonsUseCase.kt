package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.Season
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetCatalogSeasonsUseCase {
    suspend operator fun invoke(): Either<DomainError, List<Season>>
}
