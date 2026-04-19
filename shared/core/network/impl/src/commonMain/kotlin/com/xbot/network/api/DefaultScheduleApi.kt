package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.dto.ScheduleDto
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultScheduleApi(private val requester: ResilientHttpRequester) : ScheduleApi {
    override suspend fun getScheduleNow(): Either<DomainError, Map<String, List<ScheduleDto>>> = requester.request {
        get("anime/schedule/now")
    }

    override suspend fun getScheduleWeek(): Either<DomainError, List<ScheduleDto>> = requester.request {
        get("anime/schedule/week")
    }
}
