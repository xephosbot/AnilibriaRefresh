package com.xbot.network.models.entities.anime

import com.xbot.network.models.entities.common.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreApi(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("image") val image: Image? = null,
    @SerialName("total_releases") val totalReleases: Int? = null,
)