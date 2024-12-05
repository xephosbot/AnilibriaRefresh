package com.xbot.api.shared.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProductionStatusEnum(private val value: String) {
    @SerialName("IS_IN_PRODUCTION") IS_IN_PRODUCTION("IS_IN_PRODUCTION"),

    @SerialName("IS_NOT_IN_PRODUCTION") IS_NOT_IN_PRODUCTION("IS_NOT_IN_PRODUCTION"),
    ;

    override fun toString(): String = value
}
