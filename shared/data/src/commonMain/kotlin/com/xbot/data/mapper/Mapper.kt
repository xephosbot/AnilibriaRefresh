package com.xbot.data.mapper

import com.xbot.api.models.shared.EpisodeApi
import com.xbot.api.models.shared.GenreApi
import com.xbot.api.models.shared.MemberApi
import com.xbot.api.models.shared.ReleaseApi
import com.xbot.domain.models.Episode
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Member
import com.xbot.domain.models.Poster
import com.xbot.domain.models.Release
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toLocalDateTime

internal fun GenreApi.toDomain() = Genre(
    id = id,
    name = name
)

internal fun ReleaseApi.toDomain() = Release(
    id = id,
    type = type?.toDomain(),
    year = year,
    name = name.main,
    englishName = name.english,
    description = description.orEmpty(),
    episodesCount = episodesTotal,
    episodeDuration = averageDurationOfEpisode,
    favoritesCount = addedInUsersFavorites,
    poster = Poster(
        src = poster.optimized.src,
        thumbnail = poster.optimized.thumbnail,
    ),
)

internal fun EpisodeApi.toDomain() = Episode(
    id = id,
    name = name,
    englishName = nameEnglish,
    duration = duration,
    preview = Poster(
        src = preview.optimized.src,
        thumbnail = preview.optimized.thumbnail,
    ),
    hls480 = hls480,
    hls720 = hls720,
    hls1080 = hls1080,
    ordinal = ordinal,
    updatedAt = Instant.parse(
        input = updatedAt,
        format = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET
    ).toLocalDateTime(TimeZone.currentSystemDefault())
)

internal fun MemberApi.toDomain() = Member(
    id = id,
    name = nickname.orEmpty(),
    role = role?.toDomain(),
    avatar = Poster(
        src = user?.avatar?.preview,
        thumbnail = user?.avatar?.thumbnail
    )
)
