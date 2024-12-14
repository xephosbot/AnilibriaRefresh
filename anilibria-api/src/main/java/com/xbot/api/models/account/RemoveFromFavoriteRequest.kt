package com.xbot.api.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoveFromFavoriteRequest(
    @SerialName("release_id") val releaseId: Int,
)
