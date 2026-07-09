package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.models.dto.ScheduleDto
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultScheduleApi(private val requester: HttpRequester) : ScheduleApi {
    override suspend fun getScheduleNow(): Either<AppError, Map<String, List<ScheduleDto>>> = requester.request {
        get("anime/schedule/now")
    }

    override suspend fun getScheduleWeek(): Either<AppError, List<ScheduleDto>> = requester.request {
        get("anime/schedule/week")
    }
}
