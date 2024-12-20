package com.xbot.api.models.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: Int,
    @SerialName("avatar") val avatar: com.xbot.api.models.shared.User.Avatar,
) {
    @Serializable
    data class Avatar(
        @SerialName("preview") val preview: String?,
        @SerialName("thumbnail") val thumbnail: String?,
    )
}
