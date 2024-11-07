package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.ValDesc
import com.xbot.api.models.enums.AgeRatingEnum
import com.xbot.domain.model.AgeRatingEnumModel

object SuccessAgeRatingsMapper : ApiSuccessModelMapper<List<ValDesc<AgeRatingEnum>>, List<AgeRatingEnumModel>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<AgeRatingEnum>>>): List<AgeRatingEnumModel> {
        val ageRatings = apiSuccessResponse.data.map { it.value }
        return ageRatings.map { ageRating ->
            ageRating.toAgeRating()
        }
    }

    private fun AgeRatingEnum.toAgeRating(): AgeRatingEnumModel = when(this) {
        AgeRatingEnum.R0_PLUS -> AgeRatingEnumModel.R0_PLUS
        AgeRatingEnum.R6_PLUS -> AgeRatingEnumModel.R6_PLUS
        AgeRatingEnum.R12_PLUS -> AgeRatingEnumModel.R12_PLUS
        AgeRatingEnum.R16_PLUS -> AgeRatingEnumModel.R16_PLUS
        AgeRatingEnum.R18_PLUS -> AgeRatingEnumModel.R18_PLUS
    }
}