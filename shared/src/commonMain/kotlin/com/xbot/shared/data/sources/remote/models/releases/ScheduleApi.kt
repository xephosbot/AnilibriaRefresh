package com.xbot.shared.data.sources.remote.models.releases

import com.xbot.shared.data.sources.remote.models.shared.EpisodeApi
import com.xbot.shared.data.sources.remote.models.shared.ReleaseApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleApi(
    @SerialName("release") val release: ReleaseApi,
    @SerialName("new_release_episode") val newEpisode: EpisodeApi?,
    @SerialName("new_release_episode_ordinal") val newEpisodeOrdinal: Int?,
)
