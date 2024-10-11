package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Quality(
    @SerialName("value") val value: String,
    @SerialName("description") val description: String
)