package com.xbot.api.models.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HLS(
    @SerialName("fhd") val fhd: String?,
    @SerialName("hd") val hd: String?,
    @SerialName("sd") val sd: String?
)