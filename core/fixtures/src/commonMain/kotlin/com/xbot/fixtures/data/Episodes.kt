package com.xbot.fixtures.data

import com.xbot.domain.models.Episode
import com.xbot.domain.models.Poster
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

val episodeMocks = listOf(
    Episode(
        id = "1",
        name = "Episode 1",
        englishName = "The Beginning",
        ordinal = 1f,
        preview = Poster("https://example.com/ep1.jpg", null),
        hls480 = "https://example.com/ep1_480.m3u8",
        hls720 = "https://example.com/ep1_720.m3u8",
        hls1080 = "https://example.com/ep1_1080.m3u8",
        duration = 24.minutes,
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    ),
    Episode(
        id = "2",
        name = "Episode 2",
        englishName = "The Journey Continues",
        ordinal = 2f,
        preview = Poster("https://example.com/ep2.jpg", null),
        hls480 = "https://example.com/ep2_480.m3u8",
        hls720 = "https://example.com/ep2_720.m3u8",
        hls1080 = "https://example.com/ep2_1080.m3u8",
        duration = 24.minutes,
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    ),
    Episode(
        id = "3",
        name = "Episode 3",
        englishName = "The Confrontation",
        ordinal = 3f,
        preview = Poster("https://example.com/ep3.jpg", null),
        hls480 = "https://example.com/ep3_480.m3u8",
        hls720 = "https://example.com/ep3_720.m3u8",
        hls1080 = "https://example.com/ep3_1080.m3u8",
        duration = 24.minutes,
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
)
