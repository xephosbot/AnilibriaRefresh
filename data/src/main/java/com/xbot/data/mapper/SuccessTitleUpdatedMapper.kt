package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.ReleaseCatalogResponse
import com.xbot.data.models.TitlePage
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleModel

object SuccessTitleUpdatedMapper : ApiSuccessModelMapper<ReleaseCatalogResponse, TitlePage> {
    override fun map(apiSuccessResponse: ApiResponse.Success<ReleaseCatalogResponse>): TitlePage {
        val titles = apiSuccessResponse.data.data
        val total = apiSuccessResponse.data.meta.pagination.total

        return TitlePage(
            items = titles.map { title ->
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
            },
            total = total
        )
    }
}