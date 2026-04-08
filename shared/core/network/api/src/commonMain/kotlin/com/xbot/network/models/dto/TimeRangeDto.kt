package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeRangeDto(
    @SerialName("stop") val stop: Int?,
    @SerialName("start") val start: Int?,
)