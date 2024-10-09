package com.xbot.api.models.title

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Season(
    @SerialName("string") val string: String,
    @SerialName("code") val code: Int,
    @SerialName("year") val year: Int,
    @SerialName("week_day") val weekDay: Int
)