package com.xbot.domain.models

import kotlinx.datetime.LocalDate

data class ScheduleWeek(
    val days: Map<LocalDate, List<Schedule?>> = emptyMap()
)
