package com.xbot.network.models.entities.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationMeta(
    @SerialName("total") val total: Int,
    @SerialName("count") val count: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("links") val links: PaginationLinks,
)

@Serializable
data class PaginationLinks(
    @SerialName("previous") val previous: String? = null,
    @SerialName("next") val next: String? = null,
)