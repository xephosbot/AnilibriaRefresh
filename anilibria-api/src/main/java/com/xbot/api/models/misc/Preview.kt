package com.xbot.api.models.misc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    @SerialName("src") val src: String,
    @SerialName("thumbnail") val thumbnail: String
)
