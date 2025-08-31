package com.xbot.network.models.dto

import com.xbot.network.models.enums.VideoOriginTypeDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoOriginDto(
    @SerialName("id") val id: String,
    @SerialName("url") val url: String,
    @SerialName("type") val type: VideoOriginTypeDto?,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("is_announce") val isAnnounce: Boolean
)