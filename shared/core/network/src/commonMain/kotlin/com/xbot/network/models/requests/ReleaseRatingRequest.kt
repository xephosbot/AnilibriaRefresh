package com.xbot.network.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseRatingRequest(@SerialName("score") val score: Int)
