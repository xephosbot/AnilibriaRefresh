package com.xbot.network.models.enums

import com.xbot.network.utils.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = SocialTypeDto.Companion.Serializer::class)
enum class SocialTypeDto(val type: String) {
    @SerialName("vk") VK("vk"),
    @SerialName("google") GOOGLE("google"),
    @SerialName("patreon") PATREON("patreon"),
    @SerialName("discord") DISCORD("discord");

    override fun toString(): String = type

    companion object {
        object Serializer : KSerializer<SocialTypeDto?> by EnumSerializer.Companion.create<SocialTypeDto>()
    }
}