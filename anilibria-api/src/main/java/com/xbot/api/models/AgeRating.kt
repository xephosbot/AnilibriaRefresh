package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgeRating(
    @SerialName("value") val value: String,
    @SerialName("label") val label: String,
    @SerialName("is_adult") val isAdult: Boolean,
    @SerialName("description") val description: String
)
