package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.ValDesc
import com.xbot.api.models.enums.ProductionStatusEnum
import com.xbot.domain.model.ProductionStatusesEnumModel

object SuccessProductionStatusesMapper: ApiSuccessModelMapper<List<ValDesc<ProductionStatusEnum>>, List<ProductionStatusesEnumModel>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<ProductionStatusEnum>>>): List<ProductionStatusesEnumModel> {
        val productionStatuses = apiSuccessResponse.data.map { it.value }
        return productionStatuses.map { productionStatus ->
            productionStatus.toProductionStatuses()
        }
    }

    private fun ProductionStatusEnum.toProductionStatuses(): ProductionStatusesEnumModel = when(this) {
        ProductionStatusEnum.IS_IN_PRODUCTION -> ProductionStatusesEnumModel.IS_IN_PRODUCTION
        ProductionStatusEnum.IS_NOT_IN_PRODUCTION -> ProductionStatusesEnumModel.IS_NOT_IN_PRODUCTION
    }
}