package com.xbot.data.mapper

import com.xbot.domain.models.Episode
import com.xbot.domain.models.EpisodeProgress
import com.xbot.domain.models.ExternalRating
import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Poster
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.ReleaseRating
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.ScheduleType
import com.xbot.domain.models.User
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.enums.CollectionType
import com.xbot.network.models.dto.EpisodeDto
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.ExternalRatingDto
import com.xbot.network.models.dto.FranchiseDto
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ProfileDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.dto.ReleaseMemberDto
import com.xbot.network.models.dto.ReleaseRatingDto
import com.xbot.network.models.dto.ScheduleDto
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.parse
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.seconds
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
            src = src,
            thumbnail = thumbnail
        )
    }
)

internal fun ReleaseDto.toDomain() = Release(
    id = id,
    alias = alias,
    type = type?.toDomain(),
    year = year,
    season = season?.toDomain(),
    name = name.main,
    englishName = name.english,
    description = description,
    ageRating = ageRating!!.toDomain(),
    episodesCount = episodesTotal,
    episodeDuration = averageDurationOfEpisode,
    favoritesCount = addedInUsersFavorites,
    isOngoing = isOngoing,
    isInProduction = isInProduction,
    poster = poster.optimized.let { poster ->
        val src = poster.src
        val thumbnail = poster.thumbnail

        if (src == null) return@let null

        Poster(
            src = src,
            thumbnail = thumbnail
        )
    }
)

internal fun ReleaseDto.toReleaseDetails() = ReleaseDetails(
    release = this.toDomain(),
    alternativeNames = name.alternative
        ?.split(',')
        ?.map(String::trim)
        ?.filter(String::isNotEmpty)
        .orEmpty(),
    publishDay = publishDay!!.toDayOfWeek(),
    nextEpisodeNumber = nextReleaseEpisodeNumber,
    notification = notification,
    availabilityStatus = when {
        isBlockedByGeo -> AvailabilityStatus.GeoBlocked
        isBlockedByCopyrights -> AvailabilityStatus.CopyrightBlocked
        else -> AvailabilityStatus.Available
    },
    externalPlayerUrl = externalPlayer?.let { url ->
        if (url.startsWith("//")) "https:$url" else url
    },
    freshAt = freshAt?.parseIsoDateTime(),
    genres = genres?.map(GenreDto::toDomain) ?: emptyList(),
    releaseMembers = members?.map(ReleaseMemberDto::toDomain) ?: emptyList(),
    episodes = episodes?.map(EpisodeDto::toDomain) ?: emptyList(),
    rating = rating?.toDomain(),
    shikimoriRating = shikimori?.toDomain(),
    myAnimeListRating = mal?.toDomain(),
    collectionCounts = listOfNotNull(
        addedInWatchingCollection?.let { CollectionType.WATCHING to it },
        addedInPlannedCollection?.let { CollectionType.PLANNED to it },
        addedInWatchedCollection?.let { CollectionType.WATCHED to it },
        addedInPostponedCollection?.let { CollectionType.POSTPONED to it },
        addedInAbandonedCollection?.let { CollectionType.ABANDONED to it }
    ).toMap()
)

internal fun ReleaseRatingDto.toDomain() = ReleaseRating(
    average = average,
    votes = votes,
    votesByScore = distribution.mapNotNull { (score, votes) ->
        score.toIntOrNull()?.let { it to votes }
    }.toMap()
)

internal fun ExternalRatingDto.toDomain(): ExternalRating? {
    val rating = rating ?: return null
    val url = url ?: return null
    return ExternalRating(rating = rating, votes = votes ?: 0, url = url)
}

internal fun EpisodeTimecodeDto.toDomain() = EpisodeProgress(
    episodeId = releaseEpisodeId,
    position = time.seconds,
    isWatched = isWatched,
    updatedAt = updatedAt.parseIsoDateTime()
)

private fun String.parseIsoDateTime() = Instant.parse(
    input = this,
    format = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET
).toLocalDateTime(TimeZone.currentSystemDefault())

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
            src = src,
            thumbnail = thumbnail
        )
    },
    hls480 = hls480,
    hls720 = hls720,
    hls1080 = hls1080,
    ordinal = ordinal,
    updatedAt = updatedAt.parseIsoDateTime()
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
            src = src,
            thumbnail = thumbnail
        )
    }
)

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
            src = src,
            thumbnail = thumbnail
        )
    },
    isBanned = isBanned,
    createdAt = Instant.parse(
        input = createdAt,
        format = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET
    ).toLocalDateTime(TimeZone.currentSystemDefault())
)

internal fun ScheduleDto.toDomain(): Schedule? {
    val published = publishedReleaseEpisode
    val next = nextReleaseEpisodeNumber

    val status = when {
        published != null -> ScheduleType.Released(published.toDomain())
        next != null -> ScheduleType.Upcoming(next.toFloat())
        else -> return null
    }

    return Schedule(
        release = release.toDomain(),
        type = status
    )
}

internal fun FranchiseDto.toDomain() = Franchise(
    id = id,
    name = name,
    englishName = nameEnglish,
    rating = rating,
    lastYear = lastYear,
    firstYear = firstYear,
    totalReleases = totalReleases ?: 0,
    totalEpisodes = totalEpisodes,
    totalDuration = totalDuration,
    totalDurationInSeconds = totalDurationInSeconds ?: 0L,
    poster = image.let { image ->
        val src = image.preview
        val thumbnail = image.thumbnail

        if (src == null) return@let null

        Poster(
            src = src,
            thumbnail = thumbnail
        )
    },
    franchiseReleases = franchiseReleases?.map { it.release.toDomain() }
)
