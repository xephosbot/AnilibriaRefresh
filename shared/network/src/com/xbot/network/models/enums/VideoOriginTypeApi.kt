package com.xbot.network.models.enums

import com.xbot.network.serialization.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable(with = VideoOriginTypeApi.Companion.Serializer::class)
enum class VideoOriginTypeApi(val type: String) {
    YOUTUBE_PLAYLIST("YOUTUBE_PLAYLIST");

    override fun toString(): String = type

    companion object {
        object Serializer : KSerializer<VideoOriginTypeApi?> by EnumSerializer.create()
    }
}