package com.xbot.api.models.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SponsorApi(
    @SerialName("id") val id: String? = null,
)
