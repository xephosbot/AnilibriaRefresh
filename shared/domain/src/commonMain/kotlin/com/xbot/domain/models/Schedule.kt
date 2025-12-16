package com.xbot.domain.models

data class Schedule(
    val release: Release,
    val type: ScheduleType
)

sealed interface ScheduleType {
    data class Released(val episode: Episode) : ScheduleType
    data class Upcoming(val episodeOrdinal: Float) : ScheduleType
}
