package com.xbot.api.models.misc

import com.xbot.api.models.title.Title
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    @SerialName("day") val day: Int,
    @SerialName("list") val list: List<Title>
)
