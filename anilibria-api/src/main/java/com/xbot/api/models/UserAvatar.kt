package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAvatar(
    @SerialName("preview") val preview: String?,
    @SerialName("thumbnail") val thumbnail: String?
)
