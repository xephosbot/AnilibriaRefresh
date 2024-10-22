package com.xbot.api.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SocialNetworkEnum(private val type: String) {
    @SerialName("vk") VK("vk"),
    @SerialName("google") GOOGLE("google"),
    @SerialName("patreon") PATREON("patreon"),
    @SerialName("discord") DISCORD("discord");

    override fun toString(): String = type
}