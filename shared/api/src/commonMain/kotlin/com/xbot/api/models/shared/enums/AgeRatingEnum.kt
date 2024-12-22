package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object AgeRatingEnumSerializer : EnumSerializer<AgeRatingEnum>(AgeRatingEnum.generatedSerializer())

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = AgeRatingEnumSerializer::class)
@KeepGeneratedSerializer
enum class AgeRatingEnum(val value: String) {
    @SerialName("R0_PLUS") R0_PLUS("R0_PLUS"),
    @SerialName("R6_PLUS") R6_PLUS("R6_PLUS"),
    @SerialName("R12_PLUS") R12_PLUS("R12_PLUS"),
    @SerialName("R16_PLUS") R16_PLUS("R16_PLUS"),
    @SerialName("R18_PLUS") R18_PLUS("R18_PLUS");

    override fun toString(): String = value
}
