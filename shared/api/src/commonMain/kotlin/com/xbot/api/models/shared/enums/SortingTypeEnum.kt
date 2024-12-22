package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object SortingTypeEnumSerializer : EnumSerializer<SortingTypeEnum>(SortingTypeEnum.generatedSerializer())

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = SortingTypeEnumSerializer::class)
@KeepGeneratedSerializer
enum class SortingTypeEnum(private val value: String) {
    @SerialName("FRESH_AT_DESC") FRESH_AT_DESC("FRESH_AT_DESC"),
    @SerialName("FRESH_AT_ASC") FRESH_AT_ASC("FRESH_AT_ASC"),
    @SerialName("RATING_DESC") RATING_DESC("RATING_DESC"),
    @SerialName("RATING_ASC") RATING_ASC("RATING_ASC"),
    @SerialName("YEAR_DESC") YEAR_DESC("YEAR_DESC"),
    @SerialName("YEAR_ASC") YEAR_ASC("YEAR_ASC");

    override fun toString(): String = value
}
