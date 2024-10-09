package com.xbot.api.models.title

import com.xbot.api.models.common.Names
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Release(
    @SerialName("id") val id: Int,
    @SerialName("code") val code: String,
    @SerialName("ordinal") val ordinal: Int,
    @SerialName("names") val names: Names
)
