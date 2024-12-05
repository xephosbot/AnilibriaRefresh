package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.shared.ValDesc
import com.xbot.api.shared.enums.SortingTypeEnum
import com.xbot.domain.models.enums.SortingTypes

object SuccessSortingTypesMapper : ApiSuccessModelMapper<List<ValDesc<SortingTypeEnum>>, List<SortingTypes>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<SortingTypeEnum>>>): List<SortingTypes> {
        val sortingTypes = apiSuccessResponse.data.map { it.value }
        return sortingTypes.map(com.xbot.api.shared.enums.SortingTypeEnum::toSortingType)
    }
}
