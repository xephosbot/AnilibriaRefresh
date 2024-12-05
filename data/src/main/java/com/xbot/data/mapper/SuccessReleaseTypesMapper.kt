package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.shared.ValDesc
import com.xbot.api.shared.enums.ReleaseTypeEnum
import com.xbot.domain.models.enums.ReleaseType

object SuccessReleaseTypesMapper : ApiSuccessModelMapper<List<ValDesc<ReleaseTypeEnum>>, List<ReleaseType>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<ReleaseTypeEnum>>>): List<ReleaseType> {
        val releaseTypes = apiSuccessResponse.data.map { it.value }
        return releaseTypes.map(com.xbot.api.shared.enums.ReleaseTypeEnum::toReleaseType)
    }
}
