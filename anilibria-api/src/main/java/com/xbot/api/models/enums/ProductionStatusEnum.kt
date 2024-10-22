package com.xbot.api.models.enums

enum class ProductionStatusEnum(private val value: String) {
    IS_IN_PRODUCTION("IS_IN_PRODUCTION"),
    IS_NOT_IN_PRODUCTION("IS_NOT_IN_PRODUCTION");

    override fun toString(): String = value
}