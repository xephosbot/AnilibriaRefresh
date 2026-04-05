package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationLinksDto(
    @SerialName("previous") val previous: String? = null,
    @SerialName("next") val next: String? = null,
)