package com.xbot.api.models.misc

import com.xbot.api.models.title.Title
import com.xbot.api.models.external.YouTubeVideo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedItem(
    @SerialName("youtube") val youtube: YouTubeVideo? = null,
    @SerialName("title") val title: Title? = null
)
