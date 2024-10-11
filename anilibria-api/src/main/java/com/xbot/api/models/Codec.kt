package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Codec(
    @SerialName("value") val value: String,
    @SerialName("description") val description: String
)