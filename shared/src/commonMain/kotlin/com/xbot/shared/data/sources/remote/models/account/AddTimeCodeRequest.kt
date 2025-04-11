package com.xbot.shared.data.sources.remote.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddTimeCodeRequest(
    @SerialName("release_episode_id") var releaseEpisodeId: String,
    @SerialName("time") var time: Int,
    @SerialName("is_watched") var isWatched: Boolean
)
