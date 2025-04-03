package com.xbot.data.mapper

import com.xbot.api.AnilibriaApi
import com.xbot.api.models.account.ProfileApi
import com.xbot.api.models.shared.EpisodeApi
import com.xbot.api.models.shared.GenreApi
import com.xbot.api.models.shared.MemberApi
import com.xbot.api.models.shared.ReleaseApi
import com.xbot.domain.models.Episode
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Member
import com.xbot.domain.models.Poster
import com.xbot.domain.models.Profile
import com.xbot.domain.models.Release
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toLocalDateTime

internal fun GenreApi.toDomain() = Genre(
    id = id,
    name = name,
    releasesCount = totalReleases,
    image = image?.optimized?.let { image ->
        val src = image.preview
        val thumbnail = image.thumbnail

        if (src == null) return@let null

        Poster(
            src = src.let(AnilibriaApi::withBaseUrl),
            thumbnail = thumbnail?.let(AnilibriaApi::withBaseUrl),
        )
    }
)

internal fun ReleaseApi.toDomain() = Release(
    id = id,
    type = type?.toDomain(),
    year = year,
    name = name.main,
    englishName = name.english,
    description = description,
    ageRating = ageRating!!.toDomain(),
    episodesCount = episodesTotal,
    episodeDuration = averageDurationOfEpisode,
    favoritesCount = addedInUsersFavorites,
    poster = poster.optimized.let { poster ->
        val src = poster.src
        val thumbnail = poster.thumbnail

        if (src == null) return@let null

        Poster(
            src = src.let(AnilibriaApi::withBaseUrl),
            thumbnail = thumbnail?.let(AnilibriaApi::withBaseUrl),
        )
    }
)

internal fun EpisodeApi.toDomain() = Episode(
    id = id,
    name = name,
    englishName = nameEnglish,
    duration = duration,
    preview = preview.optimized.let { poster ->
        val src = poster.src
        val thumbnail = poster.thumbnail

        if (src == null) return@let null

        Poster(
            src = src.let(AnilibriaApi::withBaseUrl),
            thumbnail = thumbnail?.let(AnilibriaApi::withBaseUrl),
        )
    },
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
    avatar = user?.avatar?.let { avatar ->
        val src = avatar.preview
        val thumbnail = avatar.thumbnail

        if (src == null) return@let null

        Poster(
            src = src.let(AnilibriaApi::withBaseUrl),
            thumbnail = thumbnail?.let(AnilibriaApi::withBaseUrl),
        )
    }
)

internal fun ProfileApi.toDomain() = Profile(
    id = id,
    login = login,
    email = email,
    nickname = nickname,
    avatar = avatar.optimized.let { avatar ->
        val src = avatar.preview
        val thumbnail = avatar.thumbnail

        if (src == null) return@let null

        Poster(
            src = src.let(AnilibriaApi::withBaseUrl),
            thumbnail = thumbnail?.let(AnilibriaApi::withBaseUrl),
        )
    },
    isBanned = isBanned,
    createdAt = Instant.parse(
        input = createdAt,
        format = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET
    ).toLocalDateTime(TimeZone.currentSystemDefault())
)