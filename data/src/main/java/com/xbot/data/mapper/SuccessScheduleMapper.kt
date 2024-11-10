package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.Schedule
import com.xbot.domain.models.PosterModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek

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
                    type = title.type.value?.toReleaseType(),
                    year = title.year,
                    name = title.name.main,
                    description = title.description.orEmpty(),
                    episodesCount = title.episodesTotal,
                    favoritesCount = title.addedInUsersFavorites,
                    poster = PosterModel(
                        src = title.poster.optimized.src,
                        thumbnail = title.poster.optimized.thumbnail,
                    ),
                )
            },
        ).toSortedMap(compareBy { it.ordinal })
    }
}
