package com.xbot.api.models.releases

import com.xbot.api.models.shared.Episode
import com.xbot.api.models.shared.Release
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    @SerialName("release") val release: Release,
    @SerialName("new_release_episode") val newEpisode: Episode?,
    @SerialName("new_release_episode_ordinal") val newEpisodeOrdinal: Int,
)
