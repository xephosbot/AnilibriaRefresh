package com.xbot.network.models.dto

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class TimecodeApiTest {

    private val response = """[["68d4d5c5-e3d5-419f-a21c-c511b6b251f5", 743.5, true]]"""

    @Test
    fun decodesTuple() {
        val timecodes = Json.decodeFromString<List<TimecodeApi>>(response)

        assertEquals(
            listOf(TimecodeApi("68d4d5c5-e3d5-419f-a21c-c511b6b251f5", 743.5f, true)),
            timecodes
        )
    }

    @Test
    fun encodesBackToTuple() {
        val json = Json.encodeToString(listOf(TimecodeApi("id", 1.5f, false)))

        assertEquals("""[["id",1.5,false]]""", json)
    }

    @Test
    fun generatedSerializerCannotReadTuple() {
        assertFailsWith<SerializationException> {
            Json.decodeFromString<List<PlainTimecode>>(response)
        }
    }

    @Serializable
    private data class PlainTimecode(val episodeId: String, val time: Float, val isWatched: Boolean)
}
