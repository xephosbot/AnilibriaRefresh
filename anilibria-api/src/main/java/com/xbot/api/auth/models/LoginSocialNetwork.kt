package com.xbot.api.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginSocialNetwork(
    @SerialName("url") val url: String,
    @SerialName("state") val state: String,
)
