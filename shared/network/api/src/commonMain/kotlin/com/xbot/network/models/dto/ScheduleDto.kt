package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDto(
    @SerialName("release") val release: ReleaseDto,
    @SerialName("full_season_is_released") val fullSeasonIsReleased: Boolean,
    @SerialName("published_release_episode") val publishedReleaseEpisode: EpisodeDto?,
    @SerialName("next_release_episode_number") val nextReleaseEpisodeNumber: Int?,
)