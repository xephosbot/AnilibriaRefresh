package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Schedule
import kotlin.native.HiddenFromObjC
import kotlinx.datetime.LocalDate

@HiddenFromObjC
fun interface GetScheduleWeekUseCase {
    suspend operator fun invoke(): Either<AppError, Map<LocalDate, List<Schedule>>>
}
