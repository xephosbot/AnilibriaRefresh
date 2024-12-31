package com.xbot.api.models.shared

import com.xbot.api.models.shared.enums.MemberRoleNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberApi(
    @SerialName("id") val id: String,
    @SerialName("role") val role: MemberRoleNetwork?,
    @SerialName("nickname") val nickname: String?,
    @SerialName("user") val user: UserNetworkApi?,
)
