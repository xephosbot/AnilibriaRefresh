package com.xbot.network.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimecodeUpdateRequest(
    @SerialName("release_episode_id") val episodeId: String,
    @SerialName("time") val time: Float,
    @SerialName("is_watched") val isWatched: Boolean
)
