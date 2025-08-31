package com.xbot.network.models.enums

import com.xbot.network.utils.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = ProductionStatusDto.Companion.Serializer::class)
enum class ProductionStatusDto(val value: String) {
    @SerialName("IS_IN_PRODUCTION") IS_IN_PRODUCTION("IS_IN_PRODUCTION"),
    @SerialName("IS_NOT_IN_PRODUCTION") IS_NOT_IN_PRODUCTION("IS_NOT_IN_PRODUCTION");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<ProductionStatusDto?> by EnumSerializer.create<ProductionStatusDto>()
    }
}
