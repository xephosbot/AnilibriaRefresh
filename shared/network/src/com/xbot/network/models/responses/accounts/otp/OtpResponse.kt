package com.xbot.network.models.responses.accounts.otp

import com.xbot.network.models.entities.accounts.OtpApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpResponse(
    @SerialName("otp") val otp: OtpApi,
    @SerialName("remaining_time") val remainingTime: Int,
)