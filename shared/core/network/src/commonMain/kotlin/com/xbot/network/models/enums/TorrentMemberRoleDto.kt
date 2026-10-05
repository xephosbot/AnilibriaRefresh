package com.xbot.network.models.enums

import com.xbot.network.utils.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = TorrentMemberRoleDto.Companion.Serializer::class)
enum class TorrentMemberRoleDto(val value: String) {
    @SerialName("HEVC")
    HEVC("HEVC");

    override fun toString(): String = value

    companion object {
        object Serializer :
            KSerializer<TorrentMemberRoleDto?> by EnumSerializer.create<TorrentMemberRoleDto>()
    }
}
