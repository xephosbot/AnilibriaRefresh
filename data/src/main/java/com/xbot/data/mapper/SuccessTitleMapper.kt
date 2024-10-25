package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.Release
import com.xbot.api.models.enums.AgeRatingEnum
import com.xbot.api.models.enums.PublishDayEnum
import com.xbot.domain.model.DayOfWeek
import com.xbot.domain.model.EpisodeModel
import com.xbot.domain.model.MemberModel
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleDetailModel

object SuccessTitleMapper : ApiSuccessModelMapper<Release, TitleDetailModel> {
    override fun map(apiSuccessResponse: ApiResponse.Success<Release>): TitleDetailModel {
        val title = apiSuccessResponse.data
        return TitleDetailModel(
            id = title.id,
            type = title.type.description ?: "",
            year = title.year,
            name = title.name.main,
            season = title.season.description ?: "",
            description = title.description ?: "",
            poster = PosterModel(
                src = title.poster.optimized.src,
                thumbnail = title.poster.optimized.thumbnail
            ),
            freshAt = title.freshAt ?: "",
            createdAt = title.createdAt ?: "",
            updatedAt = title.updatedAt ?: "",
            isOngoing = title.isOngoing,
            ageRating = title.ageRating.value.toLabelString(),
            publishDay = title.publishDay.value.toDayOfWeek(),
            notification = title.notification ?: "",
            episodesTotal = title.episodesTotal,
            isInProduction = title.isInProduction,
            addedInUsersFavorites = title.addedInUsersFavorites,
            averageDurationOfEpisode = title.averageDurationOfEpisode,
            genres = title.genres?.map { it.name } ?: listOf(),
            members = title.members?.map { member ->
                MemberModel(
                    id = member.id,
                    name = member.nickname,
                    role = member.role.description ?: ""
                )
            } ?: listOf(),
            episodes = title.episodes?.map { episode ->
                EpisodeModel(
                    id = episode.id,
                    name = episode.name,
                    duration = episode.duration,
                    preview = PosterModel(
                        src = episode.preview.optimized.src,
                        thumbnail = episode.preview.optimized.thumbnail
                    ),
                    ordinal = episode.ordinal.toInt()
                )
            } ?: listOf()
        )
    }

    private fun AgeRatingEnum.toLabelString(): String = when(this) {
        AgeRatingEnum.R0_PLUS -> "0+"
        AgeRatingEnum.R6_PLUS -> "6+"
        AgeRatingEnum.R12_PLUS -> "12+"
        AgeRatingEnum.R16_PLUS -> "16+"
        AgeRatingEnum.R18_PLUS -> "18+"
    }

    private fun PublishDayEnum.toDayOfWeek(): DayOfWeek = when(this) {
        PublishDayEnum.MONDAY -> DayOfWeek.MONDAY
        PublishDayEnum.TUESDAY -> DayOfWeek.TUESDAY
        PublishDayEnum.WEDNESDAY -> DayOfWeek.WEDNESDAY
        PublishDayEnum.THURSDAY -> DayOfWeek.THURSDAY
        PublishDayEnum.FRIDAY -> DayOfWeek.FRIDAY
        PublishDayEnum.SATURDAY -> DayOfWeek.SATURDAY
        PublishDayEnum.SUNDAY -> DayOfWeek.SUNDAY
    }
}