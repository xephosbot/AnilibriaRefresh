package com.xbot.domain.usecase

import com.xbot.domain.models.ScheduleWeek
import kotlinx.coroutines.flow.Flow

fun interface GetSortedScheduleWeekUseCase {
    fun invoke(): Flow<ScheduleWeek>
}
