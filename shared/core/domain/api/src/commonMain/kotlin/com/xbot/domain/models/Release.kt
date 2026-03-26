package com.xbot.domain.models

import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import io.nlopez.asyncresult.AsyncResult
import io.nlopez.asyncresult.Loading
import io.nlopez.asyncresult.Success
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
    val season: Season?,
    val isOngoing: Boolean?,
    val publishDay: DayOfWeek?,
    val notification: String?,
    val availabilityStatus: AvailabilityStatus?,
    val genres: List<Genre>,
    val releaseMembers: List<ReleaseMember>,
    val episodes: List<Episode>,
)

data class ReleaseDetailsExtended(
    val release: AsyncResult<Release> = Loading,
    val details: AsyncResult<ReleaseDetails> = Loading,
    val relatedReleases: AsyncResult<List<Release>> = Loading,
)

fun ReleaseDetailsExtended.withRelease(release: Release): ReleaseDetailsExtended =
    copy(release = Success(release))
