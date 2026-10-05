package com.xbot.network.models.dto

import com.xbot.network.models.enums.CollectionTypeDto
import com.xbot.network.utils.JsonTupleSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = CollectionIdDto.Serializer::class)
data class CollectionIdDto(
    @SerialName("release_id") val releaseId: Int,
    @SerialName("type_of_collection") val collectionType: CollectionTypeDto
) {
    internal object Serializer : JsonTupleSerializer<CollectionIdDto>(generatedSerializer())
}
