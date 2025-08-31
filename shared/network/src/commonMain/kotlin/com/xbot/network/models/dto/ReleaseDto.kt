package com.xbot.network.models.dto

import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.PublishDayDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SeasonDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseDto(
    @SerialName("id") val id: Int,
    @SerialName("type") val type: ReleaseTypeDto?,
    @SerialName("year") val year: Int,
    @SerialName("name") val name: ReleaseNameDto,
    @SerialName("alias") val alias: String,
    @SerialName("season") val season: SeasonDto?,
    @SerialName("poster") val poster: PosterDto,
    @SerialName("fresh_at") val freshAt: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("is_ongoing") val isOngoing: Boolean,
    @SerialName("age_rating") val ageRating: AgeRatingDto?,
    @SerialName("publish_day") val publishDay: PublishDayDto?,
    @SerialName("description") val description: String?,
    @SerialName("notification") val notification: String?,
    @SerialName("episodes_total") val episodesTotal: Int?,
    @SerialName("external_player") val externalPlayer: String?,
    @SerialName("is_in_production") val isInProduction: Boolean,
    @SerialName("is_blocked_by_geo") val isBlockedByGeo: Boolean,
    @SerialName("is_blocked_by_copyrights") val isBlockedByCopyrights: Boolean,
    @SerialName("added_in_users_favorites") val addedInUsersFavorites: Int,
    @SerialName("average_duration_of_episode") val averageDurationOfEpisode: Int?,
    @SerialName("added_in_planned_collection") val addedInPlannedCollection: Int?,
    @SerialName("added_in_watched_collection") val addedInWatchedCollection: Int?,
    @SerialName("added_in_watching_collection") val addedInWatchingCollection: Int?,
    @SerialName("added_in_postponed_collection") val addedInPostponedCollection: Int?,
    @SerialName("added_in_abandoned_collection") val addedInAbandonedCollection: Int?,
    @SerialName("genres") val genres: List<GenreDto>? = null,
    @SerialName("members") val members: List<ReleaseMemberDto>? = null,
    @SerialName("sponsor") val sponsor: SponsorDto? = null,
    @SerialName("episodes") val episodes: List<EpisodeDto>? = null,
    @SerialName("torrents") val torrents: List<TorrentDto>? = null,
    @SerialName("full_season_is_released") val fullSeasonIsReleased: Boolean? = null,
    @SerialName("published_release_episode") val newReleaseEpisode: EpisodeDto? = null,
    @SerialName("next_release_episode_number") val nextReleaseEpisodeNumber: Int? = null,
)