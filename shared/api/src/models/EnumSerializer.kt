@file:Suppress("unused")

package com.xbot.api.models

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
internal open class EnumSerializer<T : Enum<T>>(
    private val values: Array<T>,
    private val valueMapper: (T) -> String
) : KSerializer<T?> {
    private var valueToEnumMap: Map<String, T> = values.associateBy(valueMapper)

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("EnumSerializer") {
        element("value", serializer<String>().descriptor)
        element("description", serializer<String>().descriptor)
    }

    override fun serialize(encoder: Encoder, value: T?) {
        if (value != null) {
            encoder.encodeString(valueMapper.invoke(value))
        } else {
            encoder.encodeNull()
        }
    }

    override fun deserialize(decoder: Decoder): T? {
        return decoder.decodeStructure(descriptor) {
            var value: String? = null

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break
                    0 -> value = decodeNullableSerializableElement(descriptor, index, serializer<String>())
                    1 -> decodeNullableSerializableElement(descriptor, index, serializer<String>())
                    else -> throw SerializationException("Unknown index $index")
                }
            }

            value?.let { valueToEnumMap[it] }
        }
    }
}