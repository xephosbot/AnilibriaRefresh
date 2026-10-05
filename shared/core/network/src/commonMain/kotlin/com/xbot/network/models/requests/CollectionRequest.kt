package com.xbot.network.models.requests

import com.xbot.network.models.enums.CollectionTypeDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionRequest(
    @SerialName("release_id") val releaseId: Int,
    @SerialName("type_of_collection") val collectionType: CollectionTypeDto
)
