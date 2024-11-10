package com.xbot.api.models

import com.xbot.api.models.enums.AgeRatingEnum
import com.xbot.api.models.enums.PublishDayEnum
import com.xbot.api.models.enums.ReleaseTypeEnum
import com.xbot.api.models.enums.SeasonEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Release(
    @SerialName("id") val id: Int,
    @SerialName("type") val type: ValDesc<ReleaseTypeEnum?>,
    @SerialName("year") val year: Int,
    @SerialName("name") val name: Name,
    @SerialName("alias") val alias: String,
    @SerialName("season") val season: ValDesc<SeasonEnum?>,
    @SerialName("poster") val poster: Poster,
    @SerialName("fresh_at") val freshAt: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("is_ongoing") val isOngoing: Boolean,
    @SerialName("age_rating") val ageRating: ValDesc<AgeRatingEnum>,
    @SerialName("publish_day") val publishDay: ValDesc<PublishDayEnum>,
    @SerialName("description") val description: String?,
    @SerialName("notification") val notification: String?,
    @SerialName("episodes_total") val episodesTotal: Int?,
    @SerialName("external_player") val externalPlayer: String?,
    @SerialName("is_in_production") val isInProduction: Boolean,
    @SerialName("is_blocked_by_geo") val isBlockedByGeo: Boolean,
    @SerialName("episodes_are_unknown") val episodesAreUnknown: Boolean,
    @SerialName("is_blocked_by_copyrights") val isBlockedByCopyrights: Boolean,
    @SerialName("added_in_users_favorites") val addedInUsersFavorites: Int,
    @SerialName("average_duration_of_episode") val averageDurationOfEpisode: Int?,
    @SerialName("genres") val genres: List<Genre>? = null,
    @SerialName("members") val members: List<Member>? = null,
    @SerialName("sponsor") val sponsor: Sponsor? = null,
    @SerialName("episodes") val episodes: List<Episode>? = null,
    @SerialName("torrents") val torrents: List<Torrent>? = null,
)
