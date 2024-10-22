package com.xbot.api.models

import com.xbot.api.models.enums.MemberRoleEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName("id") val id: String,
    @SerialName("role") val role: ValDesc<MemberRoleEnum>,
    @SerialName("nickname") val nickname: String,
    @SerialName("user") val user: User?
)