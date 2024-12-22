package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object SocialNetworkEnumSerializer : EnumSerializer<SocialNetworkEnum>(SocialNetworkEnum.generatedSerializer())

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = SocialNetworkEnumSerializer::class)
@KeepGeneratedSerializer
enum class SocialNetworkEnum(private val type: String) {
    @SerialName("vk") VK("vk"),
    @SerialName("google") GOOGLE("google"),
    @SerialName("patreon") PATREON("patreon"),
    @SerialName("discord") DISCORD("discord");

    override fun toString(): String = type
}
