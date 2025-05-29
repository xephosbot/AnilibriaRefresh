package com.xbot.network.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CollectionTypeApi(val type: String) {
    @SerialName("PLANNED") PLANNED("PLANNED"),
    @SerialName("WATCHED") WATCHED("WATCHED"),
    @SerialName("WATCHING") WATCHING("WATCHING"),
    @SerialName("POSTPONED") POSTPONED("POSTPONED"),
    @SerialName("ABANDONED") ABANDONED("ABANDONED"),;

    override fun toString(): String = type
}