package com.xbot.network.models.responses

import com.xbot.network.models.dto.OtpDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpResponse(
    @SerialName("otp") val otp: OtpDto,
    @SerialName("remaining_time") val remainingTime: Int,
)