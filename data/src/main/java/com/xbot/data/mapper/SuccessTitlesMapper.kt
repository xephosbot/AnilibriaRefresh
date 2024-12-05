package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.shared.Release
import com.xbot.domain.models.PosterModel
import com.xbot.domain.models.TitleModel

object SuccessTitlesMapper : ApiSuccessModelMapper<List<Release>, List<TitleModel>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<Release>>): List<TitleModel> {
        val titles = apiSuccessResponse.data
        return titles.map { title ->
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
        }
    }
}
