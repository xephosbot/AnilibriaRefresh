package com.xbot.network.models.dto

import com.xbot.network.utils.JsonTupleSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = TimecodeApi.Serializer::class)
data class TimecodeApi(val episodeId: String, val time: Float, val isWatched: Boolean) {

    internal object Serializer : JsonTupleSerializer<TimecodeApi>() {
        override fun fromTuple(items: List<JsonElement>): TimecodeApi {
            val (episodeId, time, isWatched) = items.map { it.jsonPrimitive }
            return TimecodeApi(episodeId.content, time.float, isWatched.boolean)
        }

        override fun toTuple(value: TimecodeApi): List<JsonElement> = listOf(
            JsonPrimitive(value.episodeId),
            JsonPrimitive(value.time),
            JsonPrimitive(value.isWatched)
        )
    }
}
