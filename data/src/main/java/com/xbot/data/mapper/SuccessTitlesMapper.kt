package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.Release
import com.xbot.api.models.enums.AgeRatingEnum
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
                tags = listOf(title.year.toString(), title.type.description ?: "", title.ageRating.value.toLabelString()),
                poster = PosterModel(
                    src = title.poster.optimized.src,
                    thumbnail = title.poster.optimized.thumbnail
                ),
                uploadedTime = title.updatedAt
            )
        }
    }

    private fun AgeRatingEnum.toLabelString(): String = when(this) {
        AgeRatingEnum.R0_PLUS -> "0+"
        AgeRatingEnum.R6_PLUS -> "6+"
        AgeRatingEnum.R12_PLUS -> "12+"
        AgeRatingEnum.R16_PLUS -> "16+"
        AgeRatingEnum.R18_PLUS -> "18+"
    }
}