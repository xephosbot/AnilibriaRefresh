package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import io.ktor.client.HttpClient
import io.ktor.client.request.get

interface ScheduleApi {
    suspend fun getScheduleNow(): Either<NetworkError, Map<String, com.xbot.network.models.dto.ScheduleDto>>
    suspend fun getScheduleWeek(): Either<NetworkError, List<com.xbot.network.models.dto.ScheduleDto>>
}

internal class DefaultScheduleApi(private val client: HttpClient) : ScheduleApi {
    override suspend fun getScheduleNow(): Either<NetworkError, Map<String, com.xbot.network.models.dto.ScheduleDto>> = client.request {
        get("anime/schedule/now")
    }

    override suspend fun getScheduleWeek(): Either<NetworkError, List<com.xbot.network.models.dto.ScheduleDto>> = client.request {
        get("anime/schedule/week")
    }
}