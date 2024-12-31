package com.xbot.api.models.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SponsorNetworkApi(
    @SerialName("id") val id: String? = null,
)
