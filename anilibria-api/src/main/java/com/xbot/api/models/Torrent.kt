package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Torrent(
    @SerialName("id") val id: Int,
    @SerialName("hash") val hash: String,
    @SerialName("size") val size: Long,
    @SerialName("type") val type: Type,
    @SerialName("label") val label: String,
    @SerialName("codec") val codec: Codec,
    @SerialName("color") val color: Color?,
    @SerialName("magnet") val magnet: String,
    @SerialName("seeders") val seeders: Int,
    @SerialName("quality") val quality: Quality,
    @SerialName("bitrate") val bitrate: Int?
)