package com.xbot.shared.data.sources.remote.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String,
)
