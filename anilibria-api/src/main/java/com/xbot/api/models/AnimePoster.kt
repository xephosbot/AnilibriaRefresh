package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimePoster(
    @SerialName("src") val src: String,
    @SerialName("thumbnail") val thumbnail: String,
    @SerialName("optimized") val optimized: OptimizedPoster
)