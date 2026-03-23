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
    val release: Release?,
    val season: Season?,
    val isOngoing: Boolean?,
    val publishDay: DayOfWeek?,
    val notification: String?,
    val availabilityStatus: AvailabilityStatus?,
    val genres: List<Genre?>,
    val releaseMembers: List<ReleaseMember?>,
    val episodes: List<Episode?>,
)

data class ReleaseDetailsExtended(
    val details: ReleaseDetails,
    val relatedReleases: List<Release?>
) {
    companion object {
        const val PLACEHOLDER_COUNT = 10
        private fun <T> emptyPlaceholderList(): List<T?> = List(PLACEHOLDER_COUNT) { null }

        fun create(
            release: Release? = null,
            details: ReleaseDetails? = null,
            relatedReleases: List<Release?>? = null
        ) = ReleaseDetailsExtended(
            details = details ?: ReleaseDetails(
                release = release,
                season = null,
                isOngoing = null,
                publishDay = null,
                notification = null,
                availabilityStatus = null,
                genres = emptyPlaceholderList(),
                releaseMembers = emptyPlaceholderList(),
                episodes = emptyPlaceholderList()
            ),
            relatedReleases = relatedReleases ?: emptyPlaceholderList()
        )
    }
}
