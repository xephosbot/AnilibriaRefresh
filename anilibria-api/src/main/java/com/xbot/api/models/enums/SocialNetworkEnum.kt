package com.xbot.api.models.enums

enum class SocialNetworkEnum(private val type: String) {
    VK("vk"),
    GOOGLE("google"),
    PATREON("patreon"),
    DISCORD("discord");

    override fun toString(): String = type
}