package com.xbot.api.models.shared.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ReleaseTypeEnum(val value: String) {
    @SerialName("TV") TV("TV"),

    @SerialName("ONA") ONA("ONA"),

    @SerialName("WEB") WEB("WEB"),

    @SerialName("OVA") OVA("OVA"),

    @SerialName("OAD") OAD("OAD"),

    @SerialName("MOVIE") MOVIE("MOVIE"),

    @SerialName("DORAMA") DORAMA("DORAMA"),

    @SerialName("SPECIAL") SPECIAL("SPECIAL"),
    ;

    override fun toString(): String = value
}
