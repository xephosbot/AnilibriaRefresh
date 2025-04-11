package com.xbot.shared.data.sources.remote.models.shared.enums

import com.xbot.shared.data.sources.remote.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object AgeRatingEnumSerializer :
    EnumSerializer<AgeRatingApi>(AgeRatingApi.entries.toTypedArray(), AgeRatingApi::value)

@Serializable(with = AgeRatingEnumSerializer::class)
enum class AgeRatingApi(val value: String) {
    @SerialName("R0_PLUS") R0_PLUS("R0_PLUS"),
    @SerialName("R6_PLUS") R6_PLUS("R6_PLUS"),
    @SerialName("R12_PLUS") R12_PLUS("R12_PLUS"),
    @SerialName("R16_PLUS") R16_PLUS("R16_PLUS"),
    @SerialName("R18_PLUS") R18_PLUS("R18_PLUS");

    override fun toString(): String = value
}
