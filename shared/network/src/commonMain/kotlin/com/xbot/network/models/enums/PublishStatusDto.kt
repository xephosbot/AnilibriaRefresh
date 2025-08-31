package com.xbot.network.models.enums

import com.xbot.network.utils.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = PublishStatusDto.Companion.Serializer::class)
enum class PublishStatusDto(val value: String) {
    @SerialName("IS_ONGOING") IS_ONGOING("IS_ONGOING"),
    @SerialName("IS_NOT_ONGOING") IS_NOT_ONGOING("IS_NOT_ONGOING");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<PublishStatusDto?> by EnumSerializer.create<PublishStatusDto>()
    }
}
