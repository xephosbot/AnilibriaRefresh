package com.xbot.data.mapper

import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Episode
import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Poster
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.User
import com.xbot.network.Constants
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.EpisodeDto
import com.xbot.network.models.dto.FranchiseDto
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ProfileDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.dto.ReleaseMemberDto
import com.xbot.network.models.dto.ScheduleDto
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.parse
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal fun GenreDto.toDomain() = Genre(
    id = id,
    name = name,
    releasesCount = totalReleases,
    image = image?.optimized?.let { image ->
        val src = image.preview
        val thumbnail = image.thumbnail

        if (src == null) return@let null

        Poster(
            src = src.let(Constants::withBaseUrl),
            thumbnail = thumbnail?.let(Constants::withBaseUrl),
        )
    }
)

internal fun ReleaseDto.toDomain() = Release(
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
            src = src.let(Constants::withBaseUrl),
            thumbnail = thumbnail?.let(Constants::withBaseUrl),
        )
    }
)

@OptIn(ExperimentalTime::class)
internal fun EpisodeDto.toDomain() = Episode(
    id = id,
    name = name,
    englishName = nameEnglish,
    duration = duration.seconds,
    preview = preview.optimized.let { poster ->
        val src = poster.src
        val thumbnail = poster.thumbnail

        if (src == null) return@let null

        Poster(
            src = src.let(Constants::withBaseUrl),
            thumbnail = thumbnail?.let(Constants::withBaseUrl),
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

internal fun ReleaseMemberDto.toDomain() = ReleaseMember(
    id = id,
    name = nickname.orEmpty(),
    role = role?.toDomain(),
    avatar = user?.avatar?.let { avatar ->
        val src = avatar.preview
        val thumbnail = avatar.thumbnail

        if (src == null) return@let null

        Poster(
            src = src.let(Constants::withBaseUrl),
            thumbnail = thumbnail?.let(Constants::withBaseUrl),
        )
    }
)

@OptIn(ExperimentalTime::class)
internal fun ProfileDto.toDomain() = User(
    id = id,
    login = login,
    email = email,
    nickname = nickname,
    avatar = avatar.optimized.let { avatar ->
        val src = avatar?.preview
        val thumbnail = avatar?.thumbnail

        if (src == null) return@let null

        Poster(
            src = src.let(Constants::withBaseUrl),
            thumbnail = thumbnail?.let(Constants::withBaseUrl),
        )
    },
    isBanned = isBanned,
    createdAt = Instant.parse(
        input = createdAt,
        format = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET
    ).toLocalDateTime(TimeZone.currentSystemDefault())
)

@OptIn(ExperimentalTime::class)
internal fun ScheduleDto.toDomain() = Schedule(
    release = release.toDomain(),
    fullSeasonIsReleased = fullSeasonIsReleased,
    publishedReleaseEpisode = publishedReleaseEpisode?.toDomain() ?: Episode(
        id = release.id.toString(),
        ordinal = nextReleaseEpisodeNumber?.toFloat() ?: -1f,
        //TODO: Update it
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    )
)

internal fun FranchiseDto.toDomain() = Franchise(
    id = id,
    name = name,
    englishName = nameEnglish,
    rating = rating,
    lastYear = lastYear,
    firstYear = firstYear,
    totalReleases = totalReleases,
    totalEpisodes = totalEpisodes,
    totalDuration = totalDuration,
    totalDurationInSeconds = totalDurationInSeconds,
    poster = image.let { image ->
        val src = image.preview
        val thumbnail = image.thumbnail

        if (src == null) return@let null

        Poster(
            src = src.let(Constants::withBaseUrl),
            thumbnail = thumbnail?.let(Constants::withBaseUrl),
        )
    },
    franchiseReleases = franchiseReleases?.map { it.release.toDomain() },
)

internal fun NetworkError.toDomain(): DomainError = when (this) {
    is NetworkError.HttpError -> DomainError.HttpError(this.code, this.message)
    is NetworkError.ConnectionError -> DomainError.ConnectionError(this.cause)
    is NetworkError.SerializationError -> DomainError.SerializationError(this.cause)
    is NetworkError.UnknownError -> DomainError.UnknownError(this.cause)
}