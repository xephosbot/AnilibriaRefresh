package com.xbot.api.genres.models

import com.xbot.api.shared.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("image") val image: com.xbot.api.shared.Image? = null,
    @SerialName("total_releases") val totalReleases: Int? = null,
)
