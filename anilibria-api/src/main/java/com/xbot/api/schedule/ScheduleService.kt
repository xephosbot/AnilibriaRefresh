package com.xbot.api.schedule

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.shared.Schedule
import retrofit2.http.GET

interface ScheduleService {
    @GET("anime/schedule/week")
    suspend fun getScheduleWeek(): ApiResponse<List<com.xbot.api.shared.Schedule>>
}