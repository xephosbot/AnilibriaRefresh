package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.ScheduleRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Schedule
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetScheduleForTodayUseCase(
    private val scheduleRepository: ScheduleRepository,
) : GetScheduleForTodayUseCase {
    override suspend fun invoke(): Either<DomainError, List<Schedule>> {
        return scheduleRepository.getScheduleNow()
    }
}
