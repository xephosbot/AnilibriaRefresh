package com.xbot.network.models.dto

import com.xbot.network.utils.JsonTupleSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = TimecodeApi.Serializer::class)
data class TimecodeApi(
    @SerialName("release_episode_id") val episodeId: String,
    @SerialName("time") val time: Float,
    @SerialName("is_watched") val isWatched: Boolean
) {
    internal object Serializer : JsonTupleSerializer<TimecodeApi>(generatedSerializer())
}
