package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.ReleaseCatalogResponse
import com.xbot.data.models.TitlePage
import com.xbot.domain.models.PosterModel
import com.xbot.domain.models.TitleModel

object SuccessTitlesUpdatedMapper : ApiSuccessModelMapper<ReleaseCatalogResponse, TitlePage> {
    override fun map(apiSuccessResponse: ApiResponse.Success<ReleaseCatalogResponse>): TitlePage {
        val titles = apiSuccessResponse.data.data
        val total = apiSuccessResponse.data.meta.pagination.total
        return TitlePage(
            items = titles.map { title ->
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
            total = total,
        )
    }
}
