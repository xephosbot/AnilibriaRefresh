package com.xbot.domain.repository

import com.xbot.domain.models.Release
import kotlinx.datetime.DayOfWeek

interface ScheduleRepository {
    suspend fun getScheduleNow(): Result<List<Release>>
    suspend fun getScheduleWeek(): Result<Map<DayOfWeek, List<Release>>>
}