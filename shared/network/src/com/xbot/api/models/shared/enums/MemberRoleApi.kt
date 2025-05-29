package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = MemberRoleApi.Companion.Serializer::class)
enum class MemberRoleApi(val value: String) {
    @SerialName("poster") POSTER("poster"),
    @SerialName("timing") TIMING("timing"),
    @SerialName("voicing") VOICING("voicing"),
    @SerialName("editing") EDITING("editing"),
    @SerialName("decorating") DECORATING("decorating"),
    @SerialName("translating") TRANSLATING("translating");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<MemberRoleApi?> by EnumSerializer.create<MemberRoleApi>()
    }
}
