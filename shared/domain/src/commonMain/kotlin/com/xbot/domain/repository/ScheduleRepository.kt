package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

interface ScheduleRepository {
    suspend fun getScheduleNow(): Either<DomainError, List<Schedule>>
    suspend fun getScheduleWeek(): Either<DomainError, Map<DayOfWeek, List<Schedule>>>
    suspend fun getCurrentDay(): Either<DomainError, LocalDate>
    suspend fun getCurrentSeason(): Either<DomainError, Season>
    suspend fun getCurrentYear(): Either<DomainError, Int>
}