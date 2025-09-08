package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.Error
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek

interface ScheduleRepository {
    suspend fun getScheduleNow(): Either<Error, List<Schedule>>
    suspend fun getScheduleWeek(): Either<Error, Map<DayOfWeek, List<Schedule>>>
    suspend fun getCurrentDay(): Either<Error, DayOfWeek>
    suspend fun getCurrentSeason(): Either<Error, Season>
    suspend fun getCurrentYear(): Either<Error, Int>
}