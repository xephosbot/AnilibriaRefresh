package com.xbot.api.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("id") var id: Int,
    @SerialName("login") var login: String,
    @SerialName("email") var email: String,
    @SerialName("nickname") var nickname: String,
    @SerialName("avatar") var avatar: Avatar,
    @SerialName("torrents") var torrents: Torrents,
    @SerialName("is_banned") var isBanned: Boolean,
    @SerialName("created_at") var createdAt: String,
    @SerialName("is_with_ads") var isWithAds: Boolean
)
