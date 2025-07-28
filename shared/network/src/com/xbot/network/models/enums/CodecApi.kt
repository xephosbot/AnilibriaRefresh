package com.xbot.network.models.enums

import com.xbot.network.serialization.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = CodecApi.Companion.Serializer::class)
enum class CodecApi(val value: String) {
    @SerialName("AV1") AV1("AV1"),
    @SerialName("x264/AVC") AVC("x264/AVC"),
    @SerialName("x265/HEVC") HEVC("x265/HEVC"),
    @SerialName("x265hq/HEVC-HQ") HEVC_HQ("x265hq/HEVC-HQ"), ;

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<CodecApi?> by EnumSerializer.create<CodecApi>()
    }
}
