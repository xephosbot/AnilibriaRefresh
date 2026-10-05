package com.xbot.network.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseIdRequest(@SerialName("release_id") val releaseId: Int)
