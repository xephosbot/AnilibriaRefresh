package com.xbot.network.models.entities.anime

import com.xbot.network.models.entities.common.Poster
import com.xbot.network.models.enums.AgeRatingApi
import com.xbot.network.models.enums.PublishDayApi
import com.xbot.network.models.enums.ReleaseTypeApi
import com.xbot.network.models.enums.SeasonApi
import com.xbot.network.models.entities.anime.SponsorApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseApi(
    @SerialName("id") val id: Int,
    @SerialName("type") val type: ReleaseTypeApi?,
    @SerialName("year") val year: Int,
    @SerialName("name") val name: Name,
    @SerialName("alias") val alias: String,
    @SerialName("season") val season: SeasonApi?,
    @SerialName("poster") val poster: Poster,
    @SerialName("fresh_at") val freshAt: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("is_ongoing") val isOngoing: Boolean,
    @SerialName("age_rating") val ageRating: AgeRatingApi?,
    @SerialName("publish_day") val publishDay: PublishDayApi?,
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
    @SerialName("genres") val genres: List<GenreApi>? = null,
    @SerialName("members") val members: List<ReleaseMemberApi>? = null,
    @SerialName("sponsor") val sponsor: SponsorApi? = null,
    @SerialName("episodes") val episodes: List<EpisodeApi>? = null,
    @SerialName("torrents") val torrents: List<TorrentApi>? = null,
    @SerialName("full_season_is_released") val fullSeasonIsReleased: Boolean? = null,
    @SerialName("published_release_episode") val newReleaseEpisode: EpisodeApi? = null,
    @SerialName("next_release_episode_number") val nextReleaseEpisodeNumber: Int? = null,
)

@Serializable
data class Name(
    @SerialName("main") val main: String,
    @SerialName("english") val english: String,
    @SerialName("alternative") val alternative: String?,
)