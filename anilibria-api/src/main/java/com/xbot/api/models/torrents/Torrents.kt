package com.xbot.api.models.torrents

import com.xbot.api.models.media.EpisodeRange
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Torrents(
    @SerialName("episodes") val episodes: EpisodeRange,
    @SerialName("list") val list: List<Torrent>
)