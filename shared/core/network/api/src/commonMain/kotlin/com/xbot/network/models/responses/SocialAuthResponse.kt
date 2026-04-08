package com.xbot.network.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialAuthResponse(
    @SerialName("url") val url: String,
    @SerialName("state") val state: String,
)