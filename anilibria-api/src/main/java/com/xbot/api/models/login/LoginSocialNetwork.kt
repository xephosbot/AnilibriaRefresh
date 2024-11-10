package com.xbot.api.models.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginSocialNetwork(
    @SerialName("url") val url: String,
    @SerialName("state") val state: String,
)
