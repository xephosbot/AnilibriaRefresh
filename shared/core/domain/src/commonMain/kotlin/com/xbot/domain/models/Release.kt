package com.xbot.domain.models

import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime

data class Release(
    val id: Int,
    val alias: String,
    val type: ReleaseType?,
    val year: Int,
    val season: Season?,
    val name: String,
    val englishName: String?,
    val description: String?,
    val ageRating: AgeRating,
    val episodesCount: Int?,
    val episodeDuration: Int?,
    val favoritesCount: Int,
    val isOngoing: Boolean,
    val isInProduction: Boolean,
    val poster: Poster?
)

val Release.isFinished: Boolean
    get() = !isOngoing && !isInProduction

data class ReleaseDetails(
    val release: Release,
    val alternativeName: String?,
    val publishDay: DayOfWeek,
    val nextEpisodeNumber: Int?,
    val notification: String?,
    val availabilityStatus: AvailabilityStatus,
    val externalPlayerUrl: String?,
    val freshAt: LocalDateTime?,
    val genres: List<Genre>,
    val releaseMembers: List<ReleaseMember>,
    val episodes: List<Episode>,
    val rating: ReleaseRating?,
    val shikimoriRating: ExternalRating?,
    val myAnimeListRating: ExternalRating?,
    val collectionCounts: Map<CollectionType, Int>
)
