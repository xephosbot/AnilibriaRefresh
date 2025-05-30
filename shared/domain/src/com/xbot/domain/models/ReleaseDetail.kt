package com.xbot.domain.models

import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek

data class ReleaseDetail(
    val release: Release,
    val season: Season?,
    val isOngoing: Boolean,
    val publishDay: DayOfWeek,
    val notification: String?,
    val availabilityStatus: AvailabilityStatus,
    val genres: List<Genre>,
    val releaseMembers: List<ReleaseMember>,
    val episodes: List<Episode>,
    val relatedReleases: List<Release>
)
