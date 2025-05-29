package com.xbot.network.models.entities.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SponsorApi(
    @SerialName("id") val id: String? = null,
)