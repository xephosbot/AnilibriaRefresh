package com.xbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName("id") val id: String,
    @SerialName("role") val role: Role,
    @SerialName("nickname") val nickname: String,
    @SerialName("user") val user: User?
)