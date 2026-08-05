package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Release
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetFranchiseReleasesUseCase {
    suspend operator fun invoke(aliasOrId: String): Either<AppError, List<Release>>
}
