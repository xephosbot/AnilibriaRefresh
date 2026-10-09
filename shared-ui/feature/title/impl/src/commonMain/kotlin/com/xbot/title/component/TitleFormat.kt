package com.xbot.title.component

import com.xbot.domain.models.Episode
import com.xbot.domain.models.EpisodeProgress
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.CollectionType
import kotlin.time.Duration

internal val CollectionStatusOrder = listOf(
    CollectionType.WATCHING,
    CollectionType.PLANNED,
    CollectionType.WATCHED,
    CollectionType.POSTPONED,
    CollectionType.ABANDONED
)

internal fun Int.formatGrouped(): String = toString()
    .reversed()
    .chunked(DIGIT_GROUP_SIZE)
    .joinToString("\u00A0")
    .reversed()

internal val Release.totalDurationMinutes: Int?
    get() = episodeDuration?.let { duration -> episodesCount?.let { it * duration } }

internal fun Episode.watchedFraction(progress: EpisodeProgress?): Float? {
    val total = duration ?: return null
    if (progress == null || progress.isWatched || total <= Duration.ZERO) return null
    return (progress.position / total).toFloat().coerceIn(0f, 1f)
}

private const val DIGIT_GROUP_SIZE = 3
