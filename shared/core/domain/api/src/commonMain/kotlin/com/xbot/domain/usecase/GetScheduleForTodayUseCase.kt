package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Schedule
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetScheduleForTodayUseCase {
    suspend operator fun invoke(): Either<AppError, List<Schedule>>
}
