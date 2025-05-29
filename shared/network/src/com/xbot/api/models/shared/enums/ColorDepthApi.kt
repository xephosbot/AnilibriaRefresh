package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = ColorDepthNetwork.Companion.Serializer::class)
enum class ColorDepthNetwork(val value: String) {
    @SerialName("8bit") Depth8BIT("8bit"),
    @SerialName("10Bit") Depth10BIT("10Bit"), ;

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<ColorDepthNetwork?> by EnumSerializer.create<ColorDepthNetwork>()
    }
}
