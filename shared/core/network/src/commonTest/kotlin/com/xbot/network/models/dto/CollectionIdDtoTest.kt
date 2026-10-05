package com.xbot.network.models.dto

import com.xbot.network.models.enums.CollectionTypeDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class CollectionIdDtoTest {

    @Test
    fun decodesTuples() {
        val ids = Json.decodeFromString<List<CollectionIdDto>>(
            """[[9919, "WATCHING"], [10303, "PLANNED"]]"""
        )

        assertEquals(
            listOf(
                CollectionIdDto(9919, CollectionTypeDto.WATCHING),
                CollectionIdDto(10303, CollectionTypeDto.PLANNED)
            ),
            ids
        )
    }

    @Test
    fun encodesBackToTuple() {
        assertEquals(
            """[[1,"WATCHED"]]""",
            Json.encodeToString(listOf(CollectionIdDto(1, CollectionTypeDto.WATCHED)))
        )
    }
}
