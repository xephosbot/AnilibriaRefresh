package com.xbot.network.models.enums

import com.xbot.network.serialization.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = ColorDepthApi.Companion.Serializer::class)
enum class ColorDepthApi(val value: String) {
    @SerialName("8bit") Depth8BIT("8bit"),
    @SerialName("10Bit") Depth10BIT("10Bit"), ;

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<ColorDepthApi?> by EnumSerializer.create<ColorDepthApi>()
    }
}
