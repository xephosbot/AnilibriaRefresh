package com.xbot.network.models.enums

import com.xbot.network.serialization.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = SortingTypeApi.Companion.Serializer::class)
enum class SortingTypeApi(val value: String) {
    @SerialName("FRESH_AT_DESC") FRESH_AT_DESC("FRESH_AT_DESC"),
    @SerialName("FRESH_AT_ASC") FRESH_AT_ASC("FRESH_AT_ASC"),
    @SerialName("RATING_DESC") RATING_DESC("RATING_DESC"),
    @SerialName("RATING_ASC") RATING_ASC("RATING_ASC"),
    @SerialName("YEAR_DESC") YEAR_DESC("YEAR_DESC"),
    @SerialName("YEAR_ASC") YEAR_ASC("YEAR_ASC");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<SortingTypeApi?> by EnumSerializer.create<SortingTypeApi>()
    }
}
