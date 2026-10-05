package com.xbot.network.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpAcceptRequest(@SerialName("code") val code: Int)
