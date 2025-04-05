package com.xbot.api.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeWithTimeCode(
    @SerialName("release_episode_id") var releaseEpisodeId: String,
    @SerialName("time") var time: Int,
    @SerialName("is_watched") var isWatched: Boolean
)
