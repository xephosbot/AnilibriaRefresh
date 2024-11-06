package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.Release
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleModel

object SuccessTitlesMapper : ApiSuccessModelMapper<List<Release>, List<TitleModel>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<Release>>): List<TitleModel> {
        val titles = apiSuccessResponse.data
        return titles.map { title ->
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
    }
}