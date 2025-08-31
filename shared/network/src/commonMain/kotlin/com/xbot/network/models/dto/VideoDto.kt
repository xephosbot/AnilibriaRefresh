package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    @SerialName("id") val id: Int,
    @SerialName("url") val url: String,
    @SerialName("title") val title: String,
    @SerialName("views") val views: Int,
    @SerialName("image") val image: ImageDto,
    @SerialName("comments") val comments: Int,
    @SerialName("video_id") val videoId: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("is_announce") val isAnnounce: Boolean,
    @SerialName("origin") val origin: VideoOriginDto
)