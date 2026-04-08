package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Schedule
import kotlinx.datetime.LocalDate

fun interface GetScheduleWeekUseCase {
    suspend operator fun invoke(): Either<DomainError, Map<LocalDate, List<Schedule>>>
}
