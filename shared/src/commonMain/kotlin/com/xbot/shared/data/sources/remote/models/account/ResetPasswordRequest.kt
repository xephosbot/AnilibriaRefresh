package com.xbot.shared.data.sources.remote.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    @SerialName("token") val token: String,
    @SerialName("password") val password: String,
    @SerialName("password_confirmation") val passwordConfirmation: String
)
