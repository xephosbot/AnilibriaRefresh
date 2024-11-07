package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.ValDesc
import com.xbot.api.models.enums.SeasonEnum
import com.xbot.domain.model.SeasonEnumModel

object SuccessSeasonMapper: ApiSuccessModelMapper<List<ValDesc<SeasonEnum>>, List<SeasonEnumModel>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<SeasonEnum>>>): List<SeasonEnumModel> {
        val seasons = apiSuccessResponse.data.map { it.value }
        return seasons.map { season ->
            season.toSeason()
        }
    }

    private fun SeasonEnum.toSeason(): SeasonEnumModel = when(this) {
        SeasonEnum.AUTUMN -> SeasonEnumModel.AUTUMN
        SeasonEnum.SPRING -> SeasonEnumModel.SPRING
        SeasonEnum.SUMMER -> SeasonEnumModel.SUMMER
        SeasonEnum.WINTER -> SeasonEnumModel.WINTER
    }
}