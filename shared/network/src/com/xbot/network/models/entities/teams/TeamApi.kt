package com.xbot.network.models.entities.teams

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamApi(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("description") val description: String,
)
