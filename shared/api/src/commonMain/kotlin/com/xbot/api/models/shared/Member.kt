package com.xbot.api.models.shared

import com.xbot.api.models.shared.enums.MemberRoleEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName("id") val id: String,
    @SerialName("role") val role: LabeledValue<MemberRoleEnum>,
    @SerialName("nickname") val nickname: String?,
    @SerialName("user") val user: User?,
)
