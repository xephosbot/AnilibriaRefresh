package com.xbot.api.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseWithCollectionType(
    @SerialName("release_id") val releaseId: Int,
    @SerialName("type_of_collection") val collectionType: String
)
