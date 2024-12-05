package com.xbot.api.shared

import com.xbot.api.shared.enums.CodecEnum
import com.xbot.api.shared.enums.ColorDepthEnum
import com.xbot.api.shared.enums.QualityEnum
import com.xbot.api.shared.enums.TorrentTypeEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Torrent(
    @SerialName("id") val id: Int,
    @SerialName("hash") val hash: String,
    @SerialName("size") val size: Long,
    @SerialName("type") val type: ValDesc<TorrentTypeEnum>,
    @SerialName("label") val label: String,
    @SerialName("magnet") val magnet: String,
    @SerialName("filename") val fileName: String,
    @SerialName("seeders") val seeders: Int,
    @SerialName("quality") val quality: ValDesc<QualityEnum>,
    @SerialName("codec") val codec: ValDesc<CodecEnum>,
    @SerialName("color") val color: ValDesc<ColorDepthEnum?>,
    @SerialName("bitrate") val bitrate: Int?,
    @SerialName("leechers") val leechers: Int,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("description") val description: String,
    @SerialName("completed_times") val completedTimes: Int,
)
