package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageOptimizedDto(
    @SerialName("preview") val preview: String?,
    @SerialName("thumbnail") val thumbnail: String?,
)