package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.ValDesc
import com.xbot.api.models.enums.ReleaseTypeEnum
import com.xbot.domain.models.enums.ReleaseType

object SuccessReleaseTypesMapper : ApiSuccessModelMapper<List<ValDesc<ReleaseTypeEnum>>, List<ReleaseType>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<ReleaseTypeEnum>>>): List<ReleaseType> {
        val releaseTypes = apiSuccessResponse.data.map { it.value }
        return releaseTypes.map(ReleaseTypeEnum::toReleaseType)
    }
}
