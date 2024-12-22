package com.xbot.api.models.shared.enums

import com.xbot.api.models.EnumSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object PublishStatusEnumSerializer : EnumSerializer<PublishStatusEnum>(PublishStatusEnum.generatedSerializer())

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = PublishStatusEnumSerializer::class)
@KeepGeneratedSerializer
enum class PublishStatusEnum(private val value: String) {
    @SerialName("IS_ONGOING") IS_ONGOING("IS_ONGOING"),
    @SerialName("IS_NOT_ONGOING") IS_NOT_ONGOING("IS_NOT_ONGOING");

    override fun toString(): String = value
}
