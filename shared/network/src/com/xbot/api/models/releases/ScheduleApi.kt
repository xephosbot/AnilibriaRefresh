package com.xbot.api.models.releases

import com.xbot.api.models.shared.EpisodeApi
import com.xbot.api.models.shared.ReleaseApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleApi(
    @SerialName("release") val release: ReleaseApi,
    @SerialName("full_season_is_released") val fullSeasonIsReleased: Boolean,
    @SerialName("published_release_episode") val publishedReleaseEpisode: EpisodeApi?,
    @SerialName("next_release_episode_number") val nextReleaseEpisodeNumber: Int?,
)
