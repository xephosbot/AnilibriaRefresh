package com.xbot.network.models.dto

import com.xbot.network.models.enums.CollectionTypeDto
import com.xbot.network.utils.JsonTupleSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = CollectionIdDto.Serializer::class)
data class CollectionIdDto(val releaseId: Int, val collectionType: CollectionTypeDto) {

    internal object Serializer : JsonTupleSerializer<CollectionIdDto>() {
        override fun fromTuple(items: List<JsonElement>): CollectionIdDto {
            val (releaseId, collectionType) = items
            return CollectionIdDto(
                releaseId = releaseId.jsonPrimitive.int,
                collectionType = Json.decodeFromJsonElement(
                    CollectionTypeDto.serializer(),
                    collectionType
                )
            )
        }

        override fun toTuple(value: CollectionIdDto): List<JsonElement> =
            listOf(JsonPrimitive(value.releaseId), JsonPrimitive(value.collectionType.type))
    }
}
