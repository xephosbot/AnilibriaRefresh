package com.xbot.api.models.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LabeledValue<T>(
    @SerialName("value") val value: T,
    @SerialName("description") val description: String?,
)
