package com.xbot.api.models.enums

enum class PublishStatusEnum(private val value: String) {
    IS_ONGOING("IS_ONGOING"),
    IS_NOT_ONGOING("IS_NOT_ONGOING");

    override fun toString(): String = value
}