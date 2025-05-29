package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = ProductionStatusApi.Companion.Serializer::class)
enum class ProductionStatusApi(val value: String) {
    @SerialName("IS_IN_PRODUCTION") IS_IN_PRODUCTION("IS_IN_PRODUCTION"),
    @SerialName("IS_NOT_IN_PRODUCTION") IS_NOT_IN_PRODUCTION("IS_NOT_IN_PRODUCTION");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<ProductionStatusApi?> by EnumSerializer.create<ProductionStatusApi>()
    }
}
