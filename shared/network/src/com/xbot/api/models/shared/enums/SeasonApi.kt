package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = SeasonApi.Companion.Serializer::class)
enum class SeasonApi(val value: String) {
    @SerialName("winter") WINTER("winter"),
    @SerialName("spring") SPRING("spring"),
    @SerialName("summer") SUMMER("summer"),
    @SerialName("autumn") AUTUMN("autumn");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<SeasonApi?> by EnumSerializer.create<SeasonApi>()
    }
}
