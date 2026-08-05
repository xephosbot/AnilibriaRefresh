package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.ScheduleDto

interface ScheduleApi {
    suspend fun getScheduleNow(): Either<AppError, Map<String, List<ScheduleDto>>>
    suspend fun getScheduleWeek(): Either<AppError, List<ScheduleDto>>
}
