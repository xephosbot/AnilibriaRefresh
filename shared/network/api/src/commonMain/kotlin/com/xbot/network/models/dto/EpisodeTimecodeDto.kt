package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeTimecodeDto(
    @SerialName("id") val id: Long,
    @SerialName("time") val time: Double,
    @SerialName("user_id") val userId: Long,
    @SerialName("is_watched") val isWatched: Boolean,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("release_episode_id") val releaseEpisodeId: String
)
