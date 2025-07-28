package com.xbot.network.requests.anime

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.ScheduleApi
import io.ktor.client.request.get

suspend fun AnilibriaClient.getScheduleNow(): Map<String, ScheduleApi> = request {
    get("anime/schedule/now")
}

suspend fun AnilibriaClient.getScheduleWeek(): List<ScheduleApi> = request {
    get("anime/schedule/week")
}