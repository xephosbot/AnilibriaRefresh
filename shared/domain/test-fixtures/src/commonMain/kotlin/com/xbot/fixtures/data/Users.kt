package com.xbot.fixtures.data

import com.xbot.domain.models.Poster
import com.xbot.domain.models.User
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

val userMock = User(
    id = 12345,
    login = "test_user",
    email = "test@example.com",
    nickname = "Test User",
    avatar = Poster("https://example.com/avatar.jpg", "https://example.com/avatar_thumb.jpg"),
    isBanned = false,
    createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)
