package com.xbot.api.models.shared.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class QualityEnum(private val value: String) {
    @SerialName("360p") RES360p("360p"),

    @SerialName("480p") RES480p("480p"),

    @SerialName("576p") RES576p("576p"),

    @SerialName("720p") RES720p("720p"),

    @SerialName("1080p") RES1080p("1080p"),

    @SerialName("2k") RES2k("2k"),

    @SerialName("4k") RES4k("4k"),

    @SerialName("8k") RES8k("8k"),
    ;

    override fun toString(): String = value
}
