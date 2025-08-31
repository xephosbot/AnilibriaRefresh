package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamUserApi(
    @SerialName("id") val id: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("is_intern") val isIntern: Boolean,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("is_vacation") val isVacation: Boolean,
    @SerialName("team") val team: TeamDto,
    @SerialName("user") val user: UserDto,
    @SerialName("roles") val roles: List<TeamRoleDto>
)
