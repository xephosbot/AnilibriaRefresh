package com.xbot.network.models.entities.media

import com.xbot.network.models.entities.common.Image
import com.xbot.network.models.enums.VideoOriginTypeApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoApi(
    @SerialName("id") val id: Int,
    @SerialName("url") val url: String,
    @SerialName("title") val title: String,
    @SerialName("views") val views: Int,
    @SerialName("image") val image: Image,
    @SerialName("comments") val comments: Int,
    @SerialName("video_id") val videoId: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("is_announce") val isAnnounce: Boolean,
    @SerialName("origin") val origin: VideoOrigin
)

@Serializable
data class VideoOrigin(
    @SerialName("id") val id: String,
    @SerialName("url") val url: String,
    @SerialName("type") val type: VideoOriginTypeApi?,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("is_announce") val isAnnounce: Boolean
)