package com.xbot.api.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: Int,
    @SerialName("avatar") val avatar: UserAvatar,
) {
    @Serializable
    data class UserAvatar(
        @SerialName("preview") val preview: String?,
        @SerialName("thumbnail") val thumbnail: String?,
    )
}
