package com.xbot.api.models.title

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Type(
    @SerialName("full_string") val fullString: String,
    @SerialName("code") val code: Int,
    @SerialName("string") val string: String,
    @SerialName("episodes") val episodes: Int?,
    @SerialName("length") val length: Int? = null
)