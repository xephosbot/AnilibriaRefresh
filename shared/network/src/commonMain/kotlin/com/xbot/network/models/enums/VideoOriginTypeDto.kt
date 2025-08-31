package com.xbot.network.models.enums

import com.xbot.network.utils.EnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable(with = VideoOriginTypeDto.Companion.Serializer::class)
enum class VideoOriginTypeDto(val type: String) {
    YOUTUBE_PLAYLIST("YOUTUBE_PLAYLIST");

    override fun toString(): String = type

    companion object {
        object Serializer : KSerializer<VideoOriginTypeDto?> by EnumSerializer.create()
    }
}