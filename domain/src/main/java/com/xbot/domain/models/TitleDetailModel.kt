package com.xbot.domain.models

import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.DayOfWeek
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season

data class TitleDetailModel(
    val id: Int,
    val type: ReleaseType?,
    val year: Int,
    val name: String,
    val season: Season?,
    val poster: PosterModel,
    val isOngoing: Boolean,
    val ageRating: AgeRating,
    val publishDay: DayOfWeek,
    val description: String,
    val notification: String,
    val episodesCount: Int?,
    val favoritesCount: Int,
    val episodeDuration: Int?,
    val genres: List<GenreModel>,
    val members: List<MemberModel>,
    val episodes: List<EpisodeModel>
)