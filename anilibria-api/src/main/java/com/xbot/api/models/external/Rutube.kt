package com.xbot.api.models.external

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rutube(
    @SerialName("episode") val episode: Float? = null,
    @SerialName("created_timestamp") val createdTimestamp: Int? = null,
    @SerialName("rutube_id") val rutubeId: String? = null
)