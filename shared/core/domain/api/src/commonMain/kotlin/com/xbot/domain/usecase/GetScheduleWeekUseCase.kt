package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Schedule
import kotlinx.datetime.LocalDate
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface GetScheduleWeekUseCase {
    suspend operator fun invoke(): Either<AppError, Map<LocalDate, List<Schedule>>>
}
