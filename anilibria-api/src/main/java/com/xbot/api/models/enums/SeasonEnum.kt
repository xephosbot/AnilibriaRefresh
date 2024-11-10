package com.xbot.api.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SeasonEnum(val value: String) {
    @SerialName("winter") WINTER("winter"),

    @SerialName("spring") SPRING("spring"),

    @SerialName("summer") SUMMER("summer"),

    @SerialName("autumn") AUTUMN("autumn"),
    ;

    override fun toString(): String = value
}
