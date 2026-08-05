package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.PublishStatus
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetCatalogPublishStatusesUseCase {
    suspend operator fun invoke(): Either<AppError, List<PublishStatus>>
}
