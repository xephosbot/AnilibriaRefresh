package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ValDesc<T>(
    @SerialName("value") val value: T,
    @SerialName("description") val description: String?,
)
