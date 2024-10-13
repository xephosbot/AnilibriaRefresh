package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SortingType(
    @SerialName("value") val value: String,
    @SerialName("label") val label: String,
    @SerialName("description") val description: String
)
