package com.xbot.data.repository

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

interface ScheduleRepository {
    suspend fun getScheduleNow(): Either<AppError, List<Schedule>>
    suspend fun getScheduleWeek(): Either<AppError, Map<DayOfWeek, List<Schedule>>>
    suspend fun getCurrentDay(): Either<AppError, LocalDate>
    suspend fun getCurrentSeason(): Either<AppError, Season>
    suspend fun getCurrentYear(): Either<AppError, Int>
}