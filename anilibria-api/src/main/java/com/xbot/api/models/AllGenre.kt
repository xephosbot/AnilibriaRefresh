package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllGenre(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("total_releases") val totalReleases: Int,
    @SerialName("image") val image: Image
)
