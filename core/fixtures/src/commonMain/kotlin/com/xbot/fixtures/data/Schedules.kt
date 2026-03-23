package com.xbot.fixtures.data

import com.xbot.domain.models.Schedule
import com.xbot.domain.models.ScheduleType

val scheduleMocks = listOf(
    Schedule(
        release = releaseMocks[0],
        type = ScheduleType.Released(episodeMocks[0])
    ),
    Schedule(
        release = releaseMocks[1],
        type = ScheduleType.Upcoming(12f)
    ),
    Schedule(
        release = releaseMocks[2],
        type = ScheduleType.Released(episodeMocks[1])
    )
)
