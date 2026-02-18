package com.xbot.domain.models

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class ScheduleWeek(
    val days: Map<LocalDate, List<Schedule?>>
) {
    companion object {
        private val EMPTY_SCHEDULES = listOf(null, null)

        fun create(
            startDate: LocalDate? = null,
            scheduleWeek: Map<DayOfWeek, List<Schedule?>>? = null
        ): ScheduleWeek {
            val baseDate = startDate ?: Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date

            val days = (0..6).associate { offset ->
                val date = baseDate.plus(offset, DateTimeUnit.DAY)
                val schedules = scheduleWeek?.get(date.dayOfWeek) ?: EMPTY_SCHEDULES
                date to schedules
            }

            return ScheduleWeek(days)
        }
    }
}