package com.xbot.network.models.entities.accounts

import com.xbot.network.models.entities.common.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileApi(
    @SerialName("id") val id: Int,
    @SerialName("login") val login: String,
    @SerialName("email") val email: String,
    @SerialName("nickname") val nickname: String?,
    @SerialName("avatar") val avatar: Image,
    @SerialName("torrents") val torrents: Torrents,
    @SerialName("is_banned") val isBanned: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("is_with_ads") val isWithAds: Boolean? = null
)

@Serializable
data class Torrents(
    @SerialName("passkey") val passkey: String,
    @SerialName("uploaded") val uploaded: Int?,
    @SerialName("downloaded") val downloaded: Int?
)
