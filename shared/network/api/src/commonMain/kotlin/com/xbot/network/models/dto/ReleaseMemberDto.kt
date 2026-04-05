package com.xbot.network.models.dto

import com.xbot.network.models.enums.MemberRoleDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseMemberDto(
    @SerialName("id") val id: String,
    @SerialName("role") val role: MemberRoleDto?,
    @SerialName("nickname") val nickname: String?,
    @SerialName("user") val user: UserDto?,
    @SerialName("external_url") val externalUrl: String? = null,
)