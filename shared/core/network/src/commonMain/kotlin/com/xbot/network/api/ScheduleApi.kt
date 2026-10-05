package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.ScheduleDto
import de.jensklingenberg.ktorfit.http.GET

interface ScheduleApi {
    @GET("anime/schedule/now")
    suspend fun getScheduleNow(): Either<AppError, Map<String, List<ScheduleDto>>>

    @GET("anime/schedule/week")
    suspend fun getScheduleWeek(): Either<AppError, List<ScheduleDto>>
}
