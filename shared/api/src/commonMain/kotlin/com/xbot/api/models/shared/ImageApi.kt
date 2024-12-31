package com.xbot.api.models.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageApi(
    @SerialName("preview") val preview: String,
    @SerialName("thumbnail") val thumbnail: String,
    @SerialName("optimized") val optimized: OptimizedImage,
) {
    @Serializable
    data class OptimizedImage(
        @SerialName("preview") val preview: String,
        @SerialName("thumbnail") val thumbnail: String,
    )
}
