package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object PublishDayEnumSerializer
    : EnumSerializer<PublishDayApi>(PublishDayApi.entries.toTypedArray(), { it.value.toString() })

@Serializable(with = PublishDayEnumSerializer::class)
enum class PublishDayApi(val value: Int) {
    @SerialName("1") MONDAY(1),
    @SerialName("2") TUESDAY(2),
    @SerialName("3") WEDNESDAY(3),
    @SerialName("4") THURSDAY(4),
    @SerialName("5") FRIDAY(5),
    @SerialName("6") SATURDAY(6),
    @SerialName("7") SUNDAY(7);

    override fun toString(): String = value.toString()
}
