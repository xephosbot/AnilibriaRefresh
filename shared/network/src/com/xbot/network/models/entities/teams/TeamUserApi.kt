package com.xbot.network.models.entities.teams

import com.xbot.network.models.entities.accounts.UserApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamUserApi(
    @SerialName("id") val id: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("is_intern") val isIntern: Boolean,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("is_vacation") val isVacation: Boolean,
    @SerialName("team") val team: TeamApi,
    @SerialName("user") val user: UserApi,
    @SerialName("roles") val roles: List<TeamRoleApi>
)
