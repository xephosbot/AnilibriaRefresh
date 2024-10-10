package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeCatalogResponse(
    @SerialName("data") val data: List<Anime>,
    @SerialName("meta") val meta: Meta
)
