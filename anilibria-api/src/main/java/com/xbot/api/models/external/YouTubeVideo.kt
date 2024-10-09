package com.xbot.api.models.external

import com.xbot.api.models.misc.Preview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YouTubeVideo(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("preview") val preview: Preview,
    @SerialName("youtube_id") val youtubeId: String,
    @SerialName("comments") val comments: Int,
    @SerialName("views") val views: Int,
    @SerialName("timestamp") val timestamp: Long
)
