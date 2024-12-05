package com.xbot.api.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseCatalogResponse(
    @SerialName("data") val data: List<Release>,
    @SerialName("meta") val meta: Meta,
) {
    @Serializable
    data class Meta(
        @SerialName("pagination") val pagination: Pagination,
    )
}
