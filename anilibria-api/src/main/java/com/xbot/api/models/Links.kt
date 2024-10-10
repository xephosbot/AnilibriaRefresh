package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Links(
    @SerialName("previous") val previous: String? = null,
    @SerialName("next") val next: String? = null
)