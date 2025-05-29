package com.xbot.network.models.responses.common

import com.xbot.network.models.entities.common.PaginationMeta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse<T>(
    @SerialName("data") val data: List<T>,
    @SerialName("meta") val meta: Meta,
)

@Serializable
data class Meta(
    @SerialName("pagination") val pagination: PaginationMeta,
)