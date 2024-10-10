package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.AnimeCatalogResponse
import com.xbot.data.models.TitlePage
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleModel

object SuccessTitleUpdatedMapper : ApiSuccessModelMapper<AnimeCatalogResponse, TitlePage> {
    override fun map(apiSuccessResponse: ApiResponse.Success<AnimeCatalogResponse>): TitlePage {
        val titles = apiSuccessResponse.data.data
        val total = apiSuccessResponse.data.meta.pagination.total

        return TitlePage(
            items = titles.map { title ->
                TitleModel(
                    id = title.id,
                    name = title.name.main,
                    description = title.description,
                    poster = PosterModel(
                        src = title.poster.optimized.src,
                        thumbnail = title.poster.optimized.thumbnail
                    ),
                    uploadedTime = title.updatedAt
                )
            },
            total = total
        )
    }
}