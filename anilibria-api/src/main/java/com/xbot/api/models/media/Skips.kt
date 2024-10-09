package com.xbot.api.models.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Skips(
    @SerialName("opening") val opening: List<String>,
    @SerialName("ending") val ending: List<String>
)