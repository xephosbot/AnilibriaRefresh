package com.xbot.api.models.title

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FranchiseDetail(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String
)