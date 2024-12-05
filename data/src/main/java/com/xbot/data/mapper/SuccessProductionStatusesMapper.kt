package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.shared.ValDesc
import com.xbot.api.shared.enums.ProductionStatusEnum
import com.xbot.domain.models.enums.ProductionStatus

object SuccessProductionStatusesMapper : ApiSuccessModelMapper<List<ValDesc<ProductionStatusEnum>>, List<ProductionStatus>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<ProductionStatusEnum>>>): List<ProductionStatus> {
        val productionStatuses = apiSuccessResponse.data.map { it.value }
        return productionStatuses.map(com.xbot.api.shared.enums.ProductionStatusEnum::toProductionStatus)
    }
}
