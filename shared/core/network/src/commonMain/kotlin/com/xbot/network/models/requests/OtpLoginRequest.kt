package com.xbot.network.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpLoginRequest(
    @SerialName("code") val code: Int,
    @SerialName("device_id") val deviceId: String
)
