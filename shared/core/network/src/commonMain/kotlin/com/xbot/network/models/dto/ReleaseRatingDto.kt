package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseRatingDto(
    @SerialName("average") val average: Double?,
    @SerialName("votes") val votes: Int,
    @SerialName("distribution") val distribution: Map<String, Int> = emptyMap()
)
