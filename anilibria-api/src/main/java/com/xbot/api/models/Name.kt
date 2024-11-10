package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Name(
    @SerialName("main") val main: String,
    @SerialName("english") val english: String,
    @SerialName("alternative") val alternative: String?,
)
