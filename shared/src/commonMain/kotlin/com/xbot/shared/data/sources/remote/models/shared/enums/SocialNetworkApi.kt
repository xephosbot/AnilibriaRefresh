package com.xbot.shared.data.sources.remote.models.shared.enums

import com.xbot.shared.data.sources.remote.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object SocialNetworkEnumSerializer
    : EnumSerializer<SocialNetwork>(SocialNetwork.entries.toTypedArray(), SocialNetwork::type)

@Serializable(with = SocialNetworkEnumSerializer::class)
enum class SocialNetwork(val type: String) {
    @SerialName("vk") VK("vk"),
    @SerialName("google") GOOGLE("google"),
    @SerialName("patreon") PATREON("patreon"),
    @SerialName("discord") DISCORD("discord");

    override fun toString(): String = type
}
