package com.xbot.network.models.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonPrimitive

/** Watch progress of one episode; the API sends it as a `[release_episode_id, time, is_watched]` tuple. */
@Serializable(with = TimecodeApi.Serializer::class)
data class TimecodeApi(val episodeId: String, val time: Float, val isWatched: Boolean) {

    internal object Serializer : KSerializer<TimecodeApi> {
        private val delegate = JsonArray.serializer()

        override val descriptor: SerialDescriptor = delegate.descriptor

        override fun deserialize(decoder: Decoder): TimecodeApi {
            val (episodeId, time, isWatched) = delegate.deserialize(decoder).map {
                it.jsonPrimitive
            }
            return TimecodeApi(episodeId.content, time.float, isWatched.boolean)
        }

        override fun serialize(encoder: Encoder, value: TimecodeApi) {
            val tuple =
                listOf(
                    JsonPrimitive(value.episodeId),
                    JsonPrimitive(value.time),
                    JsonPrimitive(value.isWatched)
                )
            delegate.serialize(encoder, JsonArray(tuple))
        }
    }
}
