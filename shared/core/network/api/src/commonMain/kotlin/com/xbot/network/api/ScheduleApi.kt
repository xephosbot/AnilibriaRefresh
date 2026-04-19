package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.ScheduleDto

interface ScheduleApi {
    suspend fun getScheduleNow(): Either<DomainError, Map<String, List<ScheduleDto>>>
    suspend fun getScheduleWeek(): Either<DomainError, List<ScheduleDto>>
}
