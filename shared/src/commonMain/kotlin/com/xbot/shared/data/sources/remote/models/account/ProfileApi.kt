package com.xbot.shared.data.sources.remote.models.account

import com.xbot.shared.data.sources.remote.models.shared.ImageApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileApi(
    @SerialName("id") val id: Int,
    @SerialName("login") val login: String,
    @SerialName("email") val email: String,
    @SerialName("nickname") val nickname: String?,
    @SerialName("avatar") val avatar: ImageApi,
    @SerialName("torrents") val torrents: Torrents,
    @SerialName("is_banned") val isBanned: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("is_with_ads") val isWithAds: Boolean? = null
)
