package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.ScheduleDto

interface ScheduleApi {
    suspend fun getScheduleNow(): Either<NetworkError, Map<String, List<ScheduleDto>>>
    suspend fun getScheduleWeek(): Either<NetworkError, List<ScheduleDto>>
}
