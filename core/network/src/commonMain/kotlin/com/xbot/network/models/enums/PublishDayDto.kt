package com.xbot.network.models.enums

import com.xbot.network.utils.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = PublishDayDto.Companion.Serializer::class)
enum class PublishDayDto(val value: Int) {
    @SerialName("1") MONDAY(1),
    @SerialName("2") TUESDAY(2),
    @SerialName("3") WEDNESDAY(3),
    @SerialName("4") THURSDAY(4),
    @SerialName("5") FRIDAY(5),
    @SerialName("6") SATURDAY(6),
    @SerialName("7") SUNDAY(7);

    override fun toString(): String = value.toString()

    companion object {
        object Serializer : KSerializer<PublishDayDto?> by EnumSerializer.create<PublishDayDto>()
    }
}
