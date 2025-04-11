package com.xbot.shared.data.sources.remote.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgetPasswordRequest(
    @SerialName("email") val email: String
)
