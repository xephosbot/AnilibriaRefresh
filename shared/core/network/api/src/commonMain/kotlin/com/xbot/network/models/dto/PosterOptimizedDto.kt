package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PosterOptimizedDto(
    @SerialName("src") val src: String?,
    @SerialName("thumbnail") val thumbnail: String?,
)