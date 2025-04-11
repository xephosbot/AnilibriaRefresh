package com.xbot.shared.data.sources.remote.models.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleasesWithMetadataApi(
    @SerialName("data") val data: List<ReleaseApi>,
    @SerialName("meta") val meta: Meta,
) {
    @Serializable
    data class Meta(
        @SerialName("pagination") val pagination: PaginationApi,
    )
}
