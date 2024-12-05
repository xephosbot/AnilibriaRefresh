package com.xbot.api.shared

import com.xbot.api.shared.enums.MemberRoleEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName("id") val id: String,
    @SerialName("role") val role: ValDesc<MemberRoleEnum>,
    @SerialName("nickname") val nickname: String?,
    @SerialName("user") val user: User?,
)
