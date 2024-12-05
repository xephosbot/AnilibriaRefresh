package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.shared.ValDesc
import com.xbot.api.shared.enums.AgeRatingEnum
import com.xbot.domain.models.enums.AgeRating

object SuccessAgeRatingsMapper : ApiSuccessModelMapper<List<ValDesc<AgeRatingEnum>>, List<AgeRating>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<AgeRatingEnum>>>): List<AgeRating> {
        val ageRatings = apiSuccessResponse.data.map { it.value }
        return ageRatings.map(com.xbot.api.shared.enums.AgeRatingEnum::toAgeRating)
    }
}
