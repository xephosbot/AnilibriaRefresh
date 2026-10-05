package com.xbot.network.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

internal abstract class JsonTupleSerializer<T>(serializer: KSerializer<T>) :
    JsonTransformingSerializer<T>(serializer) {

    private val names = serializer.descriptor.elementNames.toList()

    override fun transformDeserialize(element: JsonElement): JsonElement = buildJsonObject {
        names.zip(element.jsonArray).forEach { (name, value) -> put(name, value) }
    }

    override fun transformSerialize(element: JsonElement): JsonElement = buildJsonArray {
        names.forEach { add(element.jsonObject[it] ?: JsonNull) }
    }
}
