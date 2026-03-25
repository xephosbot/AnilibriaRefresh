package com.xbot.search.state

import com.xbot.domain.models.Genre
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

internal object GenreSerializer : KSerializer<Genre> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Genre") {
        element<Int>("id")
        element<String>("name")
    }

    override fun serialize(encoder: Encoder, value: Genre) {
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.name)
        }
    }

    override fun deserialize(decoder: Decoder): Genre {
        return decoder.decodeStructure(descriptor) {
            var id = 0
            var name = ""
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeIntElement(descriptor, 0)
                    1 -> name = decodeStringElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Genre(id = id, name = name, releasesCount = null, image = null)
        }
    }
}

internal object IntRangeSerializer : KSerializer<IntRange> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("IntRange") {
        element<Int>("start")
        element<Int>("endInclusive")
    }

    override fun serialize(encoder: Encoder, value: IntRange) {
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.first)
            encodeIntElement(descriptor, 1, value.last)
        }
    }

    override fun deserialize(decoder: Decoder): IntRange {
        return decoder.decodeStructure(descriptor) {
            var start = 0
            var endInclusive = 0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> start = decodeIntElement(descriptor, 0)
                    1 -> endInclusive = decodeIntElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            start..endInclusive
        }
    }
}
