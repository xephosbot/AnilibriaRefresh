package com.xbot.api.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Avatar(
    @SerialName("preview") var preview: String,
    @SerialName("thumbnail") var thumbnail: String,
    @SerialName("optmized") var optmized: Optimized
) {
    @Serializable
    data class Optimized(
        @SerialName("preview") var preview: String,
        @SerialName("thumbnail") var thumbnail: String
    )
}
