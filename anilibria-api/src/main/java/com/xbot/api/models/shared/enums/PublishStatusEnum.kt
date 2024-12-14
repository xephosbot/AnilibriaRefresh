package com.xbot.api.models.shared.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PublishStatusEnum(private val value: String) {
    @SerialName("IS_ONGOING") IS_ONGOING("IS_ONGOING"),

    @SerialName("IS_NOT_ONGOING") IS_NOT_ONGOING("IS_NOT_ONGOING"),
    ;

    override fun toString(): String = value
}
