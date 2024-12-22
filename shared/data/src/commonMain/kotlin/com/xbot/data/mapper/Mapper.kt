package com.xbot.data.mapper

import com.xbot.api.models.shared.Genre
import com.xbot.api.models.shared.Release
import com.xbot.domain.models.EpisodeModel
import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.MemberModel
import com.xbot.domain.models.PosterModel
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel

internal fun Genre.toGenreModel() = GenreModel(
    id = id,
    name = name
)

internal fun Release.toTitleModel() = TitleModel(
    id = id,
    type = type?.toReleaseType(),
    year = year,
    name = name.main,
    description = description.orEmpty(),
    episodesCount = episodesTotal,
    favoritesCount = addedInUsersFavorites,
    poster = PosterModel(
        src = poster.optimized.src,
        thumbnail = poster.optimized.thumbnail,
    ),
)

internal fun Release.toTitleDetailModel() = TitleDetailModel(
    id = id,
    type = type?.toReleaseType(),
    year = year,
    name = name.main,
    season = season?.toSeason(),
    poster = PosterModel(
        src = poster.optimized.src,
        thumbnail = poster.optimized.thumbnail,
    ),
    isOngoing = isOngoing,
    ageRating = ageRating.toAgeRating(),
    publishDay = publishDay.toDayOfWeek(),
    description = description.orEmpty(),
    notification = notification.orEmpty(),
    episodesCount = episodesTotal,
    favoritesCount = addedInUsersFavorites,
    episodeDuration = averageDurationOfEpisode,
    genres = genres?.map { genre ->
        GenreModel(
            id = genre.id,
            name = genre.name,
        )
    } ?: listOf(),
    members = members?.map { member ->
        MemberModel(
            id = member.id,
            name = member.nickname.orEmpty(),
            role = member.role.name,
        )
    } ?: listOf(),
    episodes = episodes?.map { episode ->
        EpisodeModel(
            id = episode.id,
            name = episode.name,
            duration = episode.duration,
            preview = PosterModel(
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