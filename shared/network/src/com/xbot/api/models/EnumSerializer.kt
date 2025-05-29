package com.xbot.api.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.reflect.KClass

/**
 * Generic serializer that accepts both plain enum value
 * or nested object { "value": ..., "description": ... }.
 */
@OptIn(ExperimentalSerializationApi::class)
internal class EnumSerializer<T: Enum<T>>(
    private val kClass: KClass<T>,
    enumValues: Array<T>
) : KSerializer<T?> {

    private val byValue: Map<String, T> = enumValues.associateBy { it.toString() }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(kClass.simpleName ?: "Enum", PrimitiveKind.STRING).nullable

    override fun deserialize(decoder: Decoder): T? {
        val input = decoder as? JsonDecoder
            ?: error("EnumSerializer works only with Json decoder")

        val element = input.decodeJsonElement()

        val key = when(element) {
            is JsonPrimitive -> {
                if (element.isString) {
                    element.content
                } else {
                    return null
                }
            }
            is JsonObject -> {
                when(val valueElement = element["value"]) {
                    null -> return null
                    is JsonNull -> return null
                    is JsonPrimitive -> valueElement.content
                    else -> error("Invalid 'value' type in enum wrapper: $valueElement")
                }
            }
            is JsonNull -> return null
            else -> error("Unexpected JSON: $element")
        }

        if (key.isBlank()) {
            return null
        }

        return byValue[key] ?: error("Unknown enum value: $key for enum ${kClass.simpleName}")
    }

    override fun serialize(encoder: Encoder, value: T?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            encoder.encodeString(value.toString())
        }
    }

    companion object {
        inline fun <reified T: Enum<T>> create(): EnumSerializer<T> {
            return EnumSerializer(T::class, enumValues<T>())
        }
    }
}