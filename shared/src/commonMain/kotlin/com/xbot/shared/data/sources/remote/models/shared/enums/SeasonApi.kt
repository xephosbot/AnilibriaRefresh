package com.xbot.shared.data.sources.remote.models.shared.enums

import com.xbot.shared.data.sources.remote.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object SeasonEnumSerializer
    : EnumSerializer<SeasonApi>(SeasonApi.entries.toTypedArray(), SeasonApi::value)

@Serializable(with = SeasonEnumSerializer::class)
enum class SeasonApi(val value: String) {
    @SerialName("winter") WINTER("winter"),
    @SerialName("spring") SPRING("spring"),
    @SerialName("summer") SUMMER("summer"),
    @SerialName("autumn") AUTUMN("autumn");

    override fun toString(): String = value
}
