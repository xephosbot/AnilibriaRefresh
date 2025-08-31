package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.Error
import com.xbot.domain.models.Release
import kotlinx.datetime.DayOfWeek

interface ScheduleRepository {
    suspend fun getScheduleNow(): Either<Error, List<Release>>
    suspend fun getScheduleWeek(): Either<Error, Map<DayOfWeek, List<Release>>>
}