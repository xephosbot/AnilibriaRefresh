package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeRange(
    @SerialName("stop") val stop: Int?,
    @SerialName("start") val start: Int?,
)
