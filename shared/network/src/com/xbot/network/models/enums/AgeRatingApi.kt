package com.xbot.network.models.enums

import com.xbot.network.serialization.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = AgeRatingApi.Companion.Serializer::class)
enum class AgeRatingApi(val value: String) {
    @SerialName("R0_PLUS") R0_PLUS("R0_PLUS"),
    @SerialName("R6_PLUS") R6_PLUS("R6_PLUS"),
    @SerialName("R12_PLUS") R12_PLUS("R12_PLUS"),
    @SerialName("R16_PLUS") R16_PLUS("R16_PLUS"),
    @SerialName("R18_PLUS") R18_PLUS("R18_PLUS");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<AgeRatingApi?> by EnumSerializer.create<AgeRatingApi>()
    }
}
