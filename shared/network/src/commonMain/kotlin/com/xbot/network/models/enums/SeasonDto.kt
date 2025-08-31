package com.xbot.network.models.enums

import com.xbot.network.utils.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = SeasonDto.Companion.Serializer::class)
enum class SeasonDto(val value: String) {
    @SerialName("winter") WINTER("winter"),
    @SerialName("spring") SPRING("spring"),
    @SerialName("summer") SUMMER("summer"),
    @SerialName("autumn") AUTUMN("autumn");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<SeasonDto?> by EnumSerializer.create<SeasonDto>()
    }
}
