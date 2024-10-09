package com.xbot.api.models.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    @SerialName("episode") val episode: Int,
    @SerialName("name") val name: String?,
    @SerialName("uuid") val uuid: String,
    @SerialName("created_timestamp") val createdTimestamp: Long,
    @SerialName("preview") val preview: String?,
    @SerialName("skips") val skips: Skips,
    @SerialName("hls") val hls: HLS
)
