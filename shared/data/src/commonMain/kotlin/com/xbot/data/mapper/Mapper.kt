package com.xbot.data.mapper

import com.xbot.api.models.shared.GenreApi
import com.xbot.api.models.shared.ReleaseApi
import com.xbot.domain.models.Episode
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Member
import com.xbot.domain.models.Poster
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.Release

internal fun GenreApi.toDomain() = Genre(
    id = id,
    name = name
)

internal fun ReleaseApi.toDomain() = Release(
    id = id,
    type = type?.toDomain(),
    year = year,
    name = name.main,
    description = description.orEmpty(),
    episodesCount = episodesTotal,
    episodeDuration = averageDurationOfEpisode,
    favoritesCount = addedInUsersFavorites,
    poster = Poster(
        src = poster.optimized.src,
        thumbnail = poster.optimized.thumbnail,
    ),
)

internal fun ReleaseApi.toReleaseDetail() = ReleaseDetail(
    id = id,
    type = type?.toDomain(),
    year = year,
    name = name.main,
    season = season?.toDomain(),
    poster = Poster(
        src = poster.optimized.src,
        thumbnail = poster.optimized.thumbnail,
    ),
    isOngoing = isOngoing,
    ageRating = ageRating!!.toDomain(),
    publishDay = publishDay!!.toDayOfWeek(),
    description = description.orEmpty(),
    notification = notification.orEmpty(),
    episodesCount = episodesTotal,
    favoritesCount = addedInUsersFavorites,
    episodeDuration = averageDurationOfEpisode,
    genres = genres?.map { genre ->
        Genre(
            id = genre.id,
            name = genre.name,
        )
    } ?: listOf(),
    members = members?.map { member ->
        Member(
            id = member.id,
            name = member.nickname.orEmpty(),
            role = member.role!!.name,
        )
    } ?: listOf(),
    episodes = episodes?.map { episode ->
        Episode(
            id = episode.id,
            name = episode.name,
            duration = episode.duration,
            preview = Poster(
                src = episode.preview.optimized.src,
                thumbnail = episode.preview.optimized.thumbnail,
            ),
            hls480 = episode.hls480,
            hls720 = episode.hls720,
            hls1080 = episode.hls1080,
            ordinal = episode.ordinal,
        )
    } ?: listOf(),
)