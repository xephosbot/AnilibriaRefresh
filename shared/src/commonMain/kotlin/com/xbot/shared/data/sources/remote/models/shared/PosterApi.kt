package com.xbot.shared.data.sources.remote.models.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PosterApi(
    @SerialName("src") val src: String?,
    @SerialName("thumbnail") val thumbnail: String?,
    @SerialName("optimized") val optimized: OptimizedPoster,
) {
    @Serializable
    data class OptimizedPoster(
        @SerialName("src") val src: String?,
        @SerialName("thumbnail") val thumbnail: String?,
    )
}
