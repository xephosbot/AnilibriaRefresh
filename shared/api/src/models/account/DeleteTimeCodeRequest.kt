package com.xbot.api.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteTimeCodeRequest(
    @SerialName("release_episode_id") var releaseEpisodeId: String,
)
