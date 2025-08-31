package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//TODO: make it typed enum
@Serializable
data class TeamRoleDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("color") val color: String?,
    @SerialName("sort_order") val sortOrder: Int,
)
