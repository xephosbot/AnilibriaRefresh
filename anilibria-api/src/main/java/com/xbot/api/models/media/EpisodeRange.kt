package com.xbot.api.models.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeRange(
    @SerialName("first") val first: Int,
    @SerialName("last") val last: Int,
    @SerialName("string") val string: String
)