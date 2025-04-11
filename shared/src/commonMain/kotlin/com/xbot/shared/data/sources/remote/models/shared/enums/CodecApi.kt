package com.xbot.shared.data.sources.remote.models.shared.enums

import com.xbot.shared.data.sources.remote.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object CodecEnumSerializer :
    EnumSerializer<CodecApi>(CodecApi.entries.toTypedArray(), CodecApi::value)

@Serializable(with = CodecEnumSerializer::class)
enum class CodecApi(val value: String) {
    @SerialName("AV1") AV1("AV1"),
    @SerialName("x264/AVC") AVC("x264/AVC"),
    @SerialName("x265/HEVC") HEVC("x265/HEVC"), ;

    override fun toString(): String = value
}
