package com.xbot.network.models.entities.anime

import com.xbot.network.models.enums.CodecNetwork
import com.xbot.network.models.enums.ColorDepthNetwork
import com.xbot.network.models.enums.QualityApi
import com.xbot.network.models.enums.TorrentTypeApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TorrentApi(
    @SerialName("id") val id: Int,
    @SerialName("hash") val hash: String,
    @SerialName("size") val size: Long,
    @SerialName("type") val type: TorrentTypeApi?,
    @SerialName("color") val color: ColorDepthNetwork?,
    @SerialName("codec") val codec: CodecNetwork?,
    @SerialName("label") val label: String,
    @SerialName("quality") val quality: QualityApi?,
    @SerialName("magnet") val magnet: String,
    @SerialName("filename") val fileName: String,
    @SerialName("seeders") val seeders: Int,
    @SerialName("bitrate") val bitrate: Int?,
    @SerialName("leechers") val leechers: Int,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("is_hardsub") val isHardsub: Boolean,
    @SerialName("description") val description: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("completed_times") val completedTimes: Int,
    @SerialName("torrent_members") val torrentMembers: List<ReleaseMemberApi>? = null,
    @SerialName("release") val release: ReleaseApi? = null
)