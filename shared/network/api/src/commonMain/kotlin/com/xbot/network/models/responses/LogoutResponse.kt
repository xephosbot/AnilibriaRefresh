package com.xbot.network.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    @SerialName("token") val token: String?,
)