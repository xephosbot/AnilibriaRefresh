package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.Release
import com.xbot.domain.models.EpisodeModel
import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.MemberModel
import com.xbot.domain.models.PosterModel
import com.xbot.domain.models.TitleDetailModel

object SuccessTitleMapper : ApiSuccessModelMapper<Release, TitleDetailModel> {
    override fun map(apiSuccessResponse: ApiResponse.Success<Release>): TitleDetailModel {
        val title = apiSuccessResponse.data
        return TitleDetailModel(
            id = title.id,
            type = title.type.value?.toReleaseType(),
            year = title.year,
            name = title.name.main,
            season = title.season.value?.toSeason(),
            poster = PosterModel(
                src = title.poster.optimized.src,
                thumbnail = title.poster.optimized.thumbnail,
            ),
            isOngoing = title.isOngoing,
            ageRating = title.ageRating.value.toAgeRating(),
            publishDay = title.publishDay.value.toDayOfWeek(),
            description = title.description.orEmpty(),
            notification = title.notification.orEmpty(),
            episodesCount = title.episodesTotal,
            favoritesCount = title.addedInUsersFavorites,
            episodeDuration = title.averageDurationOfEpisode,
            genres = title.genres?.map { genre ->
                GenreModel(
                    id = genre.id,
                    name = genre.name,
                )
            } ?: listOf(),
            members = title.members?.map { member ->
                MemberModel(
                    id = member.id,
                    name = member.nickname.orEmpty(),
                    role = member.role.description.orEmpty(),
                )
            } ?: listOf(),
            episodes = title.episodes?.map { episode ->
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
    }
}
