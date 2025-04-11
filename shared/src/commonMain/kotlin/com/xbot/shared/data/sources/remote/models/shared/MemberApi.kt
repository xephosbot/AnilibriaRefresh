package com.xbot.shared.data.sources.remote.models.shared

import com.xbot.shared.data.sources.remote.models.shared.enums.MemberRoleApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberApi(
    @SerialName("id") val id: String,
    @SerialName("role") val role: MemberRoleApi?,
    @SerialName("nickname") val nickname: String?,
    @SerialName("user") val user: UserApi?,
)
