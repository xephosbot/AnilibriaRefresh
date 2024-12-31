package com.xbot.domain.models

import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek

data class ReleaseDetail(
    val id: Int,
    val type: ReleaseType?,
    val year: Int,
    val name: String,
    val season: Season?,
    val poster: Poster,
    val isOngoing: Boolean,
    val ageRating: AgeRating,
    val publishDay: DayOfWeek,
    val description: String,
    val notification: String,
    val episodesCount: Int?,
    val favoritesCount: Int,
    val episodeDuration: Int?,
    val genres: List<Genre>,
    val members: List<Member>,
    val episodes: List<Episode>,
)
