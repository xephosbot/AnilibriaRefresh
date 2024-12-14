package com.xbot.api.models.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("image") val image: com.xbot.api.models.shared.Image? = null,
    @SerialName("total_releases") val totalReleases: Int? = null,
)
