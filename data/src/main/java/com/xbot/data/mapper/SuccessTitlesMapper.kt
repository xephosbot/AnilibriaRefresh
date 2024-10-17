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
                name = title.name.main,
                description = title.description ?: "",
                tags = listOf(title.year.toString(), title.type.description ?: "", title.genres?.get(0)?.name ?: ""),
                poster = PosterModel(
                    src = title.poster.optimized.src,
                    thumbnail = title.poster.optimized.thumbnail
                ),
                uploadedTime = title.updatedAt
            )
        }
    }
}