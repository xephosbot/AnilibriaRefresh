package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Schedule

fun interface GetScheduleForTodayUseCase {
    suspend operator fun invoke(): Either<DomainError, List<Schedule>>
}