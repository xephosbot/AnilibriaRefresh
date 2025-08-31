package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseNameDto(
    @SerialName("main") val main: String,
    @SerialName("english") val english: String,
    @SerialName("alternative") val alternative: String?,
)