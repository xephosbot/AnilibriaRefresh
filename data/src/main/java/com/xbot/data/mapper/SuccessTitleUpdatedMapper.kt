package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.misc.TitleUpdate
import com.xbot.data.models.TitlePage
import com.xbot.domain.model.TitleModel

object SuccessTitleUpdatedMapper : ApiSuccessModelMapper<TitleUpdate, TitlePage> {
    override fun map(apiSuccessResponse: ApiResponse.Success<TitleUpdate>): TitlePage {
        val titles = apiSuccessResponse.data.list
        val total = apiSuccessResponse.data.pagination.totalItems
        return TitlePage(
            items = titles.map { title ->
                TitleModel(
                    id = title.id,
                    name = title.names.ru ?: UNKNOWN_NAME,
                    description = title.description,
                    posterUrl = title.posters?.small?.url,
                    uploadedTime = title.updated
                )
            },
            total = total
        )
    }

    private const val UNKNOWN_NAME = "Unknown Name"
}