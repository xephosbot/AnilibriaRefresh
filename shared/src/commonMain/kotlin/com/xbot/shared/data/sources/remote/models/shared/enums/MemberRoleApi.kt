package com.xbot.shared.data.sources.remote.models.shared.enums

import com.xbot.shared.data.sources.remote.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object MemberRoleEnumSerializer
    : EnumSerializer<MemberRoleApi>(MemberRoleApi.entries.toTypedArray(), MemberRoleApi::value)

@Serializable(with = MemberRoleEnumSerializer::class)
enum class MemberRoleApi(val value: String) {
    @SerialName("poster") POSTER("poster"),
    @SerialName("timing") TIMING("timing"),
    @SerialName("voicing") VOICING("voicing"),
    @SerialName("editing") EDITING("editing"),
    @SerialName("decorating") DECORATING("decorating"),
    @SerialName("translating") TRANSLATING("translating");

    override fun toString(): String = value
}
