package com.xbot.network.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeIdRequest(@SerialName("release_episode_id") val episodeId: String)
