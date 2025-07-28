package com.xbot.network.models.entities.accounts

import com.xbot.network.models.entities.common.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserApi(
    @SerialName("id") val id: Int,
    @SerialName("avatar") val avatar: Image,
)