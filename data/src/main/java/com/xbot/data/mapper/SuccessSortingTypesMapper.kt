package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.ValDesc
import com.xbot.api.models.enums.SortingTypeEnum
import com.xbot.domain.model.SortingTypesEnumModel

object SuccessSortingTypesMapper: ApiSuccessModelMapper<List<ValDesc<SortingTypeEnum>>, List<SortingTypesEnumModel>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<SortingTypeEnum>>>): List<SortingTypesEnumModel> {
        val sortingTypes = apiSuccessResponse.data.map { it.value }
        return sortingTypes.map { sortingType ->
            sortingType.toSortingType()
        }
    }

    private fun SortingTypeEnum.toSortingType(): SortingTypesEnumModel = when(this) {
        SortingTypeEnum.YEAR_ASC -> SortingTypesEnumModel.YEAR_ASC
        SortingTypeEnum.YEAR_DESC -> SortingTypesEnumModel.YEAR_DESC
        SortingTypeEnum.RATING_ASC -> SortingTypesEnumModel.RATING_ASC
        SortingTypeEnum.RATING_DESC -> SortingTypesEnumModel.RATING_DESC
        SortingTypeEnum.FRESH_AT_ASC -> SortingTypesEnumModel.FRESH_AT_ASC
        SortingTypeEnum.FRESH_AT_DESC -> SortingTypesEnumModel.FRESH_AT_DESC
    }
}