package com.xbot.domain.fixtures

import com.xbot.domain.models.Release
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.ScheduleType

fun createSchedule(
    release: Release = createRelease(),
    type: ScheduleType = ScheduleType.Released(createEpisode())
) = Schedule(
    release = release,
    type = type
)

object ScheduleFixtures {
    val released = createSchedule(
        release = ReleaseFixtures.frieren,
        type = ScheduleType.Released(EpisodeFixtures.episode1)
    )

    val upcoming = createSchedule(
        release = ReleaseFixtures.oshiNoKo,
        type = ScheduleType.Upcoming(12f)
    )

    val anotherReleased = createSchedule(
        release = ReleaseFixtures.jujutsuKaisen,
        type = ScheduleType.Released(EpisodeFixtures.episode2)
    )

    val all = listOf(released, upcoming, anotherReleased)

    fun list(count: Int = 3) = all.take(count)
}
