package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PosterDto(
    @SerialName("src") val src: String?,
    @SerialName("preview") val preview: String? = null,
    @SerialName("thumbnail") val thumbnail: String?,
    @SerialName("optimized") val optimized: PosterOptimizedDto
)
