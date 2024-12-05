package com.xbot.api.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    @SerialName("total") val total: Int,
    @SerialName("count") val count: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("links") val links: Links,
) {
    @Serializable
    data class Links(
        @SerialName("previous") val previous: String? = null,
        @SerialName("next") val next: String? = null,
    )
}