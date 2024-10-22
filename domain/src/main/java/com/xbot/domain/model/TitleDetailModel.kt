package com.xbot.domain.model

data class TitleDetailModel(
    val id: Int,
    val type: String,
    val year: Int,
    val name: String,
    val season: String,
    val poster: PosterModel,
    val freshAt: String,
    val createdAt: String,
    val updatedAt: String,
    val isOngoing: Boolean,
    val ageRating: String,
    val publishDay: DayOfWeek,
    val description: String,
    val notification: String,
    val episodesTotal: Int?,
    val isInProduction: Boolean,
    val addedInUsersFavorites: Int,
    val averageDurationOfEpisode: Int?,
    val genres: List<String>,
    val members: List<MemberModel>,
    val episodes: List<EpisodeModel>
)

enum class DayOfWeek(val value: Int) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);
}