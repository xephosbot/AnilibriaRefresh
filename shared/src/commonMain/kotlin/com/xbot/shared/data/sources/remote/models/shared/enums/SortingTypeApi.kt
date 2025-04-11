package com.xbot.shared.data.sources.remote.models.shared.enums

import com.xbot.shared.data.sources.remote.models.EnumSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object SortingTypeEnumSerializer
    : EnumSerializer<SortingTypeApi>(SortingTypeApi.entries.toTypedArray(), SortingTypeApi::value)

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = SortingTypeEnumSerializer::class)
@KeepGeneratedSerializer
enum class SortingTypeApi(val value: String) {
    @SerialName("FRESH_AT_DESC") FRESH_AT_DESC("FRESH_AT_DESC"),
    @SerialName("FRESH_AT_ASC") FRESH_AT_ASC("FRESH_AT_ASC"),
    @SerialName("RATING_DESC") RATING_DESC("RATING_DESC"),
    @SerialName("RATING_ASC") RATING_ASC("RATING_ASC"),
    @SerialName("YEAR_DESC") YEAR_DESC("YEAR_DESC"),
    @SerialName("YEAR_ASC") YEAR_ASC("YEAR_ASC");

    override fun toString(): String = value
}
