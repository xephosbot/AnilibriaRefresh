package com.xbot.api.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sponsor(
    @SerialName("id") val id: String? = null,
)
