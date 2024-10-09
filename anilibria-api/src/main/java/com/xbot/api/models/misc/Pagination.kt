package com.xbot.api.models.misc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    @SerialName("pages") val pages: Int,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("items_per_page") val itemsPerPage: Int,
    @SerialName("total_items") val totalItems: Int
)
