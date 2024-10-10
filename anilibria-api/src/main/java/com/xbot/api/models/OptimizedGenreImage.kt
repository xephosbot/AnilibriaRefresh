package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OptimizedGenreImage(
    @SerialName("preview") val preview: String,
    @SerialName("thumbnail") val thumbnail: String
)