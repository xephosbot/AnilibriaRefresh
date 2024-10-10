package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PublishDay(
    @SerialName("value") val value: Int,
    @SerialName("description") val description: String
)
