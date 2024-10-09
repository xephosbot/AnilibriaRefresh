package com.xbot.api.models.media

import com.xbot.api.models.common.ImageDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Posters(
    @SerialName("small") val small: ImageDetail,
    @SerialName("medium") val medium: ImageDetail,
    @SerialName("original") val original: ImageDetail
)