package com.xbot.domain.models

import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek

data class ReleaseDetail(
    val release: Release,
    val season: Season?,
    val isOngoing: Boolean,
    val ageRating: AgeRating,
    val publishDay: DayOfWeek,
    val notification: String,
    val genres: List<Genre>,
    val members: List<Member>,
    val episodes: List<Episode>,
    val relatedReleases: List<Release>
)
