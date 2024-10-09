package com.xbot.api.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Names(
    @SerialName("ru") val ru: String?,
    @SerialName("en") val en: String?,
    @SerialName("alternative") val alternative: String?
)