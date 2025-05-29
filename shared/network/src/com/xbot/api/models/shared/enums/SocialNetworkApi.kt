package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = SocialNetworkApi.Companion.Serializer::class)
enum class SocialNetworkApi(val type: String) {
    @SerialName("vk") VK("vk"),
    @SerialName("google") GOOGLE("google"),
    @SerialName("patreon") PATREON("patreon"),
    @SerialName("discord") DISCORD("discord");

    override fun toString(): String = type

    companion object {
        object Serializer : KSerializer<SocialNetworkApi?> by EnumSerializer.create<SocialNetworkApi>()
    }
}
