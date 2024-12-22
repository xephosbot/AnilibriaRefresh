@file:Suppress("unused")

package com.xbot.api.models

import com.xbot.api.di.defaultJson
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
open class EnumSerializer<T : Enum<T>>(
    private val serializer: KSerializer<T>
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(serializer.descriptor.serialName) {
        element("value", serializer<String>().descriptor)
        element("description", serializer<String>().descriptor)
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeSerializableValue(serializer, value)
    }

    override fun deserialize(decoder: Decoder): T {
        return decoder.decodeStructure(descriptor) {
            lateinit var value: String
            lateinit var description: String

            while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break
                    0 -> value = decodeStringElement(descriptor, i)
                    1 -> description = decodeStringElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }

            defaultJson.decodeFromString(serializer, value)
        }
    }
}