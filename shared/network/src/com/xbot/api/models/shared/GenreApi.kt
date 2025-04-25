package com.xbot.api.models.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreApi(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("image") val image: ImageApi? = null,
    @SerialName("total_releases") val totalReleases: Int? = null,
)
