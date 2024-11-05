package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.Schedule
import com.xbot.api.models.enums.AgeRatingEnum
import com.xbot.api.models.enums.PublishDayEnum
import com.xbot.domain.model.DayOfWeek
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleModel

object SuccessScheduleMapper : ApiSuccessModelMapper<List<Schedule>, Map<DayOfWeek, List<TitleModel>>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<Schedule>>): Map<DayOfWeek, List<TitleModel>> {
        val titles = apiSuccessResponse.data.map { it.release }
        return titles.groupBy(
            keySelector = { title ->
                title.publishDay.value.toDayOfWeek()
            },
            valueTransform = { title ->
                TitleModel(
                    id = title.id,
                    name = title.name.main,
                    description = title.description ?: "",
                    tags = listOf(title.year.toString(), title.type.description ?: "", title.ageRating.value.toLabelString()),
                    poster = PosterModel(
                        src = title.poster.optimized.src,
                        thumbnail = title.poster.optimized.thumbnail
                    ),
                    uploadedTime = title.updatedAt
                )
            }
        ).toSortedMap(compareBy { it.value })
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