package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object ColorDepthEnumSerializer : EnumSerializer<ColorDepthEnum>(ColorDepthEnum.generatedSerializer())

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = ColorDepthEnumSerializer::class)
@KeepGeneratedSerializer
enum class ColorDepthEnum(val value: String) {
    @SerialName("8bit") Depth8BIT("8bit"),
    @SerialName("10Bit") Depth10BIT("10Bit"), ;

    override fun toString(): String = value
}
