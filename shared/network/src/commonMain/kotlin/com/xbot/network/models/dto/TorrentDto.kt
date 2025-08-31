package com.xbot.network.models.dto

import com.xbot.network.models.enums.CodecDto
import com.xbot.network.models.enums.ColorDepthDto
import com.xbot.network.models.enums.QualityDto
import com.xbot.network.models.enums.TorrentTypeDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TorrentDto(
    @SerialName("id") val id: Int,
    @SerialName("hash") val hash: String,
    @SerialName("size") val size: Long,
    @SerialName("type") val type: TorrentTypeDto?,
    @SerialName("color") val color: ColorDepthDto?,
    @SerialName("codec") val codec: CodecDto?,
    @SerialName("label") val label: String,
    @SerialName("quality") val quality: QualityDto?,
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
    @SerialName("torrent_members") val torrentMembers: List<ReleaseMemberDto>? = null,
    @SerialName("release") val release: ReleaseDto? = null
)