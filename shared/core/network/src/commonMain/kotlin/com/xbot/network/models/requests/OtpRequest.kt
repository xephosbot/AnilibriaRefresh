package com.xbot.network.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(@SerialName("device_id") val deviceId: String)
