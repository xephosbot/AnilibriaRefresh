package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object CodecEnumSerializer : EnumSerializer<CodecEnum>(CodecEnum.generatedSerializer())

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = CodecEnumSerializer::class)
@KeepGeneratedSerializer
enum class CodecEnum(val value: String) {
    @SerialName("AV1") AV1("AV1"),
    @SerialName("x264/AVC") AVC("x264/AVC"),
    @SerialName("x265/HEVC") HEVC("x265/HEVC"), ;

    override fun toString(): String = value
}
