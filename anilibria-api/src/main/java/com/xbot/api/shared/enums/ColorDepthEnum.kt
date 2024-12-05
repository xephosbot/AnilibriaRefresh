package com.xbot.api.shared.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ColorDepthEnum(val value: String) {
    @SerialName("8bit") Depth8BIT("8bit"),

    @SerialName("10Bit") Depth10BIT("10Bit"),
    ;

    override fun toString(): String = value
}
