package com.xbot.network.models.enums

import com.xbot.network.utils.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = MemberRoleDto.Companion.Serializer::class)
enum class MemberRoleDto(val value: String) {
    @SerialName("poster") POSTER("poster"),
    @SerialName("timing") TIMING("timing"),
    @SerialName("voicing") VOICING("voicing"),
    @SerialName("editing") EDITING("editing"),
    @SerialName("decorating") DECORATING("decorating"),
    @SerialName("translating") TRANSLATING("translating");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<MemberRoleDto?> by EnumSerializer.create<MemberRoleDto>()
    }
}
