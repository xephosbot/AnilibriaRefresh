package com.xbot.shared.data.sources.remote.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginSocialNetworkResponse(
    @SerialName("url") val url: String,
    @SerialName("state") val state: String,
)
