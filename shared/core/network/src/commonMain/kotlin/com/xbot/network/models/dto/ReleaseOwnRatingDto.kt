package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseOwnRatingDto(
    @SerialName("release_id") val releaseId: Int,
    @SerialName("score") val score: Int?
)
