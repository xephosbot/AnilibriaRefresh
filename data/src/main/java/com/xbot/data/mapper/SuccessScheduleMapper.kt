package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.Schedule
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
                    year = title.year,
                    type = title.type.description ?: "",
                    name = title.name.main,
                    description = title.description ?: "",
                    episodesCount = title.episodesTotal,
                    favoritesCount = title.addedInUsersFavorites,
                    poster = PosterModel(
                        src = title.poster.optimized.src,
                        thumbnail = title.poster.optimized.thumbnail
                    )
                )
            }
        ).toSortedMap(compareBy { it.value })
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