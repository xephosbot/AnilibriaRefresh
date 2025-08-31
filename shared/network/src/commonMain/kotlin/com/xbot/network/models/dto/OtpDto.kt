package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpDto(
    @SerialName("code") val code: String,
    @SerialName("user_id") val userId: Int,
    @SerialName("device_id") val deviceId: String,
    @SerialName("expired_at") val expiredAt: String,
)