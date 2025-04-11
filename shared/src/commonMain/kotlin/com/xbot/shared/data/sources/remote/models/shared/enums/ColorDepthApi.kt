package com.xbot.shared.data.sources.remote.models.shared.enums

import com.xbot.shared.data.sources.remote.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object ColorDepthEnumSerializer
    : EnumSerializer<ColorDepthNetwork>(ColorDepthNetwork.entries.toTypedArray(), ColorDepthNetwork::value)

@Serializable(with = ColorDepthEnumSerializer::class)
enum class ColorDepthNetwork(val value: String) {
    @SerialName("8bit") Depth8BIT("8bit"),
    @SerialName("10Bit") Depth10BIT("10Bit"), ;

    override fun toString(): String = value
}
