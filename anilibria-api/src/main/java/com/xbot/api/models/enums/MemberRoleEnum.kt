package com.xbot.api.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MemberRoleEnum(val value: String) {
    @SerialName("poster") POSTER("poster"),

    @SerialName("timing") TIMING("timing"),

    @SerialName("voicing") VOICING("voicing"),

    @SerialName("editing") EDITING("editing"),

    @SerialName("decorating") DECORATING("decorating"),

    @SerialName("translating") TRANSLATING("translating"),
    ;

    override fun toString(): String = value
}
