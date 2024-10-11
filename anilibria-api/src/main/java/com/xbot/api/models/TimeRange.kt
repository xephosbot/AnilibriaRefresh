package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeRange(
    @SerialName("stop") val stop: String?,
    @SerialName("start") val start: String?
)