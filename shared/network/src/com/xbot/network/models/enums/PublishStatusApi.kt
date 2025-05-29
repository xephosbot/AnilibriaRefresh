package com.xbot.network.models.enums

import com.xbot.network.serialization.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = PublishStatusApi.Companion.Serializer::class)
enum class PublishStatusApi(val value: String) {
    @SerialName("IS_ONGOING") IS_ONGOING("IS_ONGOING"),
    @SerialName("IS_NOT_ONGOING") IS_NOT_ONGOING("IS_NOT_ONGOING");

    override fun toString(): String = value

    companion object {
        object Serializer : KSerializer<PublishStatusApi?> by EnumSerializer.create<PublishStatusApi>()
    }
}
