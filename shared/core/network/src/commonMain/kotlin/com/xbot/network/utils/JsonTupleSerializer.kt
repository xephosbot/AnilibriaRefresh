package com.xbot.network.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

internal abstract class JsonTupleSerializer<T> : KSerializer<T> {
    private val delegate = JsonArray.serializer()

    override val descriptor: SerialDescriptor = delegate.descriptor

    protected abstract fun fromTuple(items: List<JsonElement>): T

    protected abstract fun toTuple(value: T): List<JsonElement>

    override fun deserialize(decoder: Decoder): T = fromTuple(delegate.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: T) {
        delegate.serialize(encoder, JsonArray(toTuple(value)))
    }
}
