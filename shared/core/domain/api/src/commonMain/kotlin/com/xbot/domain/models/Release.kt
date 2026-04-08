package com.xbot.domain.models

import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek

data class Release(
    val id: Int,
    val type: ReleaseType?,
    val year: Int,
    val name: String,
    val englishName: String?,
    val description: String?,
    val ageRating: AgeRating,
    val episodesCount: Int?,
    val episodeDuration: Int?,
    val favoritesCount: Int,
    val poster: Poster?,
)

data class ReleaseDetails(
    val release: Release,
    val season: Season?,
    val isOngoing: Boolean,
    val publishDay: DayOfWeek,
    val notification: String?,
    val availabilityStatus: AvailabilityStatus,
    val genres: List<Genre>,
    val releaseMembers: List<ReleaseMember>,
    val episodes: List<Episode>,
)
