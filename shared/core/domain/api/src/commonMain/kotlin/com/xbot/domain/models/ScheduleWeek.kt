package com.xbot.domain.models

import io.nlopez.asyncresult.AsyncResult
import io.nlopez.asyncresult.Loading
import kotlinx.datetime.LocalDate

data class ScheduleWeek(
    val days: AsyncResult<Map<LocalDate, List<Schedule>>> = Loading
)
