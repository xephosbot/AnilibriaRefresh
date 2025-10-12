package com.xbot.domain.usecase

import arrow.core.Either
import arrow.core.raise.either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Schedule
import com.xbot.domain.repository.ScheduleRepository
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class GetSortedScheduleWeekUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(): Either<DomainError, Map<LocalDate, List<Schedule>>> = either {
        val currentDate = scheduleRepository.getCurrentDay().bind()
        val scheduleWeek = scheduleRepository.getScheduleWeek().bind()

        val allDaysOfWeek = DayOfWeek.entries
        val currentDayOfWeek = currentDate.dayOfWeek

        val currentDayIndex = allDaysOfWeek.indexOf(currentDayOfWeek)
        val reorderedDays = allDaysOfWeek.drop(currentDayIndex) + allDaysOfWeek.take(currentDayIndex)

        reorderedDays.mapIndexed { index, dayOfWeek ->
            val date = currentDate.plus(index, DateTimeUnit.DAY)
            val schedules = scheduleWeek[dayOfWeek] ?: emptyList()
            date to schedules
        }.toMap()
    }
}