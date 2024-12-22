package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object MemberRoleEnumSerializer : EnumSerializer<MemberRoleEnum>(MemberRoleEnum.generatedSerializer())

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = MemberRoleEnumSerializer::class)
@KeepGeneratedSerializer
enum class MemberRoleEnum(val value: String) {
    @SerialName("poster") POSTER("poster"),
    @SerialName("timing") TIMING("timing"),
    @SerialName("voicing") VOICING("voicing"),
    @SerialName("editing") EDITING("editing"),
    @SerialName("decorating") DECORATING("decorating"),
    @SerialName("translating") TRANSLATING("translating");

    override fun toString(): String = value
}
