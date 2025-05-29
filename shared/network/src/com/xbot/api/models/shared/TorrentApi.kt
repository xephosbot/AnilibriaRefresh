package com.xbot.api.models.shared

import com.xbot.api.models.shared.enums.CodecNetwork
import com.xbot.api.models.shared.enums.ColorDepthNetwork
import com.xbot.api.models.shared.enums.QualityApi
import com.xbot.api.models.shared.enums.TorrentTypeApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TorrentApi(
    @SerialName("id") val id: Int,
    @SerialName("hash") val hash: String,
    @SerialName("size") val size: Long,
    @SerialName("type") val type: TorrentTypeApi?,
    @SerialName("label") val label: String,
    @SerialName("magnet") val magnet: String,
    @SerialName("filename") val fileName: String,
    @SerialName("seeders") val seeders: Int,
    @SerialName("quality") val quality: QualityApi?,
    @SerialName("codec") val codec: CodecNetwork?,
    @SerialName("color") val color: ColorDepthNetwork?,
    @SerialName("bitrate") val bitrate: Int?,
    @SerialName("leechers") val leechers: Int,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("description") val description: String,
    @SerialName("completed_times") val completedTimes: Int,
)
