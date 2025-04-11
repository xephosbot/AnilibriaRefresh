package com.xbot.shared.data.sources.remote.models.shared.enums

import com.xbot.shared.data.sources.remote.models.EnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object ProductionStatusEnumSerializer
    : EnumSerializer<ProductionStatusApi>(ProductionStatusApi.entries.toTypedArray(), ProductionStatusApi::value)

@Serializable(with = ProductionStatusEnumSerializer::class)
enum class ProductionStatusApi(val value: String) {
    @SerialName("IS_IN_PRODUCTION") IS_IN_PRODUCTION("IS_IN_PRODUCTION"),
    @SerialName("IS_NOT_IN_PRODUCTION") IS_NOT_IN_PRODUCTION("IS_NOT_IN_PRODUCTION");

    override fun toString(): String = value
}
