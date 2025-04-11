package com.xbot.shared.data.sources.remote.models.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserApi(
    @SerialName("id") val id: Int,
    @SerialName("avatar") val avatar: Avatar,
) {
    @Serializable
    data class Avatar(
        @SerialName("preview") val preview: String?,
        @SerialName("thumbnail") val thumbnail: String?,
    )
}
