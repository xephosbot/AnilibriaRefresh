package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.raise.context.bind
import arrow.core.raise.context.either
import com.xbot.data.repository.ScheduleRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Schedule
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetScheduleWeekUseCase(
    private val scheduleRepository: ScheduleRepository
) : GetScheduleWeekUseCase {
    override suspend fun invoke(): Either<DomainError, Map<LocalDate, List<Schedule>>> = either {
        val startDate = scheduleRepository.getCurrentDay().bind()
        val scheduleWeek = scheduleRepository.getScheduleWeek().bind()

        return@either (0..6).mapNotNull { offset ->
            val date = startDate.plus(offset, DateTimeUnit.DAY)
            scheduleWeek[date.dayOfWeek]?.let { date to it }
        }.toMap()
    }
}