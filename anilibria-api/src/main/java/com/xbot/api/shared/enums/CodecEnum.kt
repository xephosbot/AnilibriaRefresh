package com.xbot.api.shared.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CodecEnum(val value: String) {
    @SerialName("AV1") AV1("AV1"),

    @SerialName("x264/AVC") AVC("x264/AVC"),

    @SerialName("x265/HEVC") HEVC("x265/HEVC"),
    ;

    override fun toString(): String = value
}
