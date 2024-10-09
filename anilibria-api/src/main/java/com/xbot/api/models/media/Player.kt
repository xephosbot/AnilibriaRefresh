package com.xbot.api.models.media

import com.xbot.api.models.external.Rutube
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    @SerialName("alternative_player") val alternativePlayer: String?,
    @SerialName("host") val host: String,
    @SerialName("is_rutube") val isRutube: Boolean,
    @SerialName("episodes") val episodes: EpisodeRange,
    @SerialName("list") val list: Map<Int, Episode>,
    @SerialName("rutube") val rutube: Rutube
)