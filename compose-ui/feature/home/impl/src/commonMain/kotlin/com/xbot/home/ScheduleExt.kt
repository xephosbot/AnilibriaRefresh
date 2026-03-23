package com.xbot.home

import androidx.compose.runtime.Composable
import com.xbot.domain.models.Episode
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.ScheduleType
import com.xbot.resources.Res
import com.xbot.resources.label_schedule_now
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Schedule.toEpisode(): Episode {
    return when (val type = this.type) {
        is ScheduleType.Released -> type.episode
        is ScheduleType.Upcoming -> Episode(
            id = this.release.id.toString(),
            ordinal = type.episodeOrdinal,
            name = stringResource(Res.string.label_schedule_now),
            updatedAt = null
        )
    }
}
