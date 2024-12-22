package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object SeasonEnumSerializer : EnumSerializer<SeasonEnum>(SeasonEnum.generatedSerializer())

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = SeasonEnumSerializer::class)
@KeepGeneratedSerializer
enum class SeasonEnum(val value: String) {
    @SerialName("winter") WINTER("winter"),
    @SerialName("spring") SPRING("spring"),
    @SerialName("summer") SUMMER("summer"),
    @SerialName("autumn") AUTUMN("autumn");

    override fun toString(): String = value
}
