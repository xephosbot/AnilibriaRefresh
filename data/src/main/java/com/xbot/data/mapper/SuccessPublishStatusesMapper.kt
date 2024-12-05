package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.shared.ValDesc
import com.xbot.api.shared.enums.PublishStatusEnum
import com.xbot.domain.models.enums.PublishStatus

object SuccessPublishStatusesMapper : ApiSuccessModelMapper<List<ValDesc<PublishStatusEnum>>, List<PublishStatus>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<PublishStatusEnum>>>): List<PublishStatus> {
        val publishStatuses = apiSuccessResponse.data.map { it.value }
        return publishStatuses.map(com.xbot.api.shared.enums.PublishStatusEnum::toPublishStatus)
    }
}
