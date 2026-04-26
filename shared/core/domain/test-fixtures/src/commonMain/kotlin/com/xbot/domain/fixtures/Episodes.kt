package com.xbot.domain.fixtures

import com.xbot.domain.models.Episode
import com.xbot.domain.models.Poster
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun createEpisode(
    id: String = "1",
    name: String? = "Episode 1",
    englishName: String? = "The Beginning",
    ordinal: Float = 1f,
    preview: Poster? = Poster("https://example.com/ep1.jpg", null),
    hls480: String? = "https://example.com/ep1_480.m3u8",
    hls720: String? = "https://example.com/ep1_720.m3u8",
    hls1080: String? = "https://example.com/ep1_1080.m3u8",
    duration: Duration? = 24.minutes,
    updatedAt: LocalDateTime? = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
) = Episode(
    id = id,
    name = name,
    englishName = englishName,
    ordinal = ordinal,
    preview = preview,
    hls480 = hls480,
    hls720 = hls720,
    hls1080 = hls1080,
    duration = duration,
    updatedAt = updatedAt
)

object EpisodeFixtures {
    val episode1 = createEpisode(
        id = "1",
        name = "Episode 1",
        englishName = "The Beginning",
        ordinal = 1f
    )

    val episode2 = createEpisode(
        id = "2",
        name = "Episode 2",
        englishName = "The Journey Continues",
        ordinal = 2f,
        preview = Poster("https://example.com/ep2.jpg", null)
    )

    val episode3 = createEpisode(
        id = "3",
        name = "Episode 3",
        englishName = "The Confrontation",
        ordinal = 3f,
        preview = Poster("https://example.com/ep3.jpg", null)
    )

    val all = listOf(episode1, episode2, episode3)

    fun list(count: Int = 3) = List(count) { i ->
        createReleaseEpisode(id = (i + 1).toString(), ordinal = (i + 1).toFloat())
    }
}

private fun createReleaseEpisode(id: String, ordinal: Float) = createEpisode(
    id = id,
    name = "Episode $id",
    englishName = "English Name $id",
    ordinal = ordinal
)

@Deprecated("Use EpisodeFixtures.all", ReplaceWith("EpisodeFixtures.all"))
val episodeMocks = EpisodeFixtures.all
