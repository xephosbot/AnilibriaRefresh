package com.xbot.api.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Team(
    @SerialName("voice") val voice: List<String>,
    @SerialName("translator") val translator: List<String>,
    @SerialName("editing") val editing: List<String>,
    @SerialName("decor") val decor: List<String>,
    @SerialName("timing") val timing: List<String>
)