package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.ValDesc
import com.xbot.api.models.enums.PublishStatusEnum
import com.xbot.domain.model.PublishStatusEnumModel

object SuccessPublishStatusMapper: ApiSuccessModelMapper<List<ValDesc<PublishStatusEnum>>, List<PublishStatusEnumModel>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<PublishStatusEnum>>>): List<PublishStatusEnumModel> {
        val publishStatuses = apiSuccessResponse.data.map { it.value }
        return publishStatuses.map { publishStatus ->
            publishStatus.toPublishStatus()
        }
    }

    private fun PublishStatusEnum.toPublishStatus(): PublishStatusEnumModel = when(this) {
        PublishStatusEnum.IS_ONGOING -> PublishStatusEnumModel.IS_ONGOING
        PublishStatusEnum.IS_NOT_ONGOING -> PublishStatusEnumModel.IS_NOT_ONGOING
    }
}