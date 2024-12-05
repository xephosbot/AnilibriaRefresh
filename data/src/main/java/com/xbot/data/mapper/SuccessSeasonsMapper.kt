package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.shared.ValDesc
import com.xbot.api.shared.enums.SeasonEnum
import com.xbot.domain.models.enums.Season

object SuccessSeasonsMapper : ApiSuccessModelMapper<List<ValDesc<SeasonEnum>>, List<Season>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<SeasonEnum>>>): List<Season> {
        val seasons = apiSuccessResponse.data.map { it.value }
        return seasons.map(com.xbot.api.shared.enums.SeasonEnum::toSeason)
    }
}
