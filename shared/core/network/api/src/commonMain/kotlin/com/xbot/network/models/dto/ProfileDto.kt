package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("id") val id: Int,
    @SerialName("login") val login: String,
    @SerialName("email") val email: String,
    @SerialName("nickname") val nickname: String?,
    @SerialName("avatar") val avatar: ImageDto,
    @SerialName("torrents") val torrents: ProfileTorrentsDto,
    @SerialName("is_banned") val isBanned: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("is_with_ads") val isWithAds: Boolean? = null
)
