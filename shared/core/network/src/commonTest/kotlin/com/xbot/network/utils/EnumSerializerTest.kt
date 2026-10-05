package com.xbot.network.utils

import com.xbot.network.models.dto.ReleaseMemberDto
import com.xbot.network.models.enums.MemberRoleDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class EnumSerializerTest {

    private val json = Json { ignoreUnknownKeys = true }

    private fun member(role: String) = json.decodeFromString<ReleaseMemberDto>(
        """{"id": "1", "role": $role, "nickname": "Lupin", "user": null}"""
    )

    @Test
    fun readsWrappedValue() {
        assertEquals(
            MemberRoleDto.HEVC,
            member("""{"value": "hevc", "description": "Кодирование HEVC"}""").role
        )
    }

    @Test
    fun readsPlainValue() {
        assertEquals(MemberRoleDto.VOICING, member("\"voicing\"").role)
    }
}
