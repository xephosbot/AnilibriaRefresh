package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object ProductionStatusEnumSerializer : EnumSerializer<ProductionStatusEnum>(ProductionStatusEnum.generatedSerializer())

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = ProductionStatusEnumSerializer::class)
@KeepGeneratedSerializer
enum class ProductionStatusEnum(private val value: String) {
    @SerialName("IS_IN_PRODUCTION") IS_IN_PRODUCTION("IS_IN_PRODUCTION"),
    @SerialName("IS_NOT_IN_PRODUCTION") IS_NOT_IN_PRODUCTION("IS_NOT_IN_PRODUCTION");

    override fun toString(): String = value
}
