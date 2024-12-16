package com.xbot.api.models.shared

import com.xbot.api.models.shared.enums.CodecEnum
import com.xbot.api.models.shared.enums.ColorDepthEnum
import com.xbot.api.models.shared.enums.QualityEnum
import com.xbot.api.models.shared.enums.TorrentTypeEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Torrent(
    @SerialName("id") val id: Int,
    @SerialName("hash") val hash: String,
    @SerialName("size") val size: Long,
    @SerialName("type") val type: LabeledValue<TorrentTypeEnum>,
    @SerialName("label") val label: String,
    @SerialName("magnet") val magnet: String,
    @SerialName("filename") val fileName: String,
    @SerialName("seeders") val seeders: Int,
    @SerialName("quality") val quality: LabeledValue<QualityEnum>,
    @SerialName("codec") val codec: LabeledValue<CodecEnum>,
    @SerialName("color") val color: LabeledValue<ColorDepthEnum?>,
    @SerialName("bitrate") val bitrate: Int?,
    @SerialName("leechers") val leechers: Int,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("description") val description: String,
    @SerialName("completed_times") val completedTimes: Int,
)
