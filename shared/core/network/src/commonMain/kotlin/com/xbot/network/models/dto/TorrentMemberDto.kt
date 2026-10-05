package com.xbot.network.models.dto

import com.xbot.network.models.enums.TorrentMemberRoleDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TorrentMemberDto(
    @SerialName("id") val id: String,
    @SerialName("role") val role: TorrentMemberRoleDto?,
    @SerialName("nickname") val nickname: String?,
    @SerialName("external_url") val externalUrl: String? = null,
    @SerialName("user") val user: UserDto? = null
)
