package com.xbot.network.models.entities.anime

import com.xbot.network.models.enums.MemberRoleApi
import com.xbot.network.models.entities.accounts.UserApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseMemberApi(
    @SerialName("id") val id: String,
    @SerialName("role") val role: MemberRoleApi?,
    @SerialName("nickname") val nickname: String?,
    @SerialName("user") val user: UserApi?,
    @SerialName("external_url") val externalUrl: String? = null,
)