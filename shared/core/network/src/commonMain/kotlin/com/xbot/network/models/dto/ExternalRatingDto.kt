package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExternalRatingDto(
    @SerialName("id") val id: Int?,
    @SerialName("url") val url: String?,
    @SerialName("votes") val votes: Int?,
    @SerialName("rating") val rating: Double?
)
