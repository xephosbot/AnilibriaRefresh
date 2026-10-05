package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SponsorDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("description") val description: String?,
    @SerialName("url_title") val urlTitle: String?,
    @SerialName("url") val url: String?
)
