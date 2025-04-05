package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object CodecEnumSerializer :
    EnumSerializer<CodecNetwork>(CodecNetwork.entries.toTypedArray(), CodecNetwork::value)

@Serializable(with = CodecEnumSerializer::class)
enum class CodecNetwork(val value: String) {
    @SerialName("AV1") AV1("AV1"),
    @SerialName("x264/AVC") AVC("x264/AVC"),
    @SerialName("x265/HEVC") HEVC("x265/HEVC"), ;

    override fun toString(): String = value
}
