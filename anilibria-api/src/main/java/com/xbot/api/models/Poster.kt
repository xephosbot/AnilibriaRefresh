package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Poster(
    @SerialName("src") val src: String?,
    @SerialName("thumbnail") val thumbnail: String?,
    @SerialName("optimized") val optimized: OptimizedPoster
)

@Serializable
data class OptimizedPoster(
    @SerialName("src") val src: String?,
    @SerialName("thumbnail") val thumbnail: String?
)