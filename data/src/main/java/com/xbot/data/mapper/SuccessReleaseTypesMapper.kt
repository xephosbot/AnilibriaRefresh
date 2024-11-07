package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.ValDesc
import com.xbot.api.models.enums.ReleaseTypeEnum
import com.xbot.domain.model.ReleaseTypeEnumModel

object SuccessReleaseTypesMapper: ApiSuccessModelMapper<List<ValDesc<ReleaseTypeEnum>>, List<ReleaseTypeEnumModel>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<ValDesc<ReleaseTypeEnum>>>): List<ReleaseTypeEnumModel> {
        val releaseTypes = apiSuccessResponse.data.map { it.value }
        return releaseTypes.map { releaseType ->
            releaseType.toReleaseType()
        }
    }

    private fun ReleaseTypeEnum.toReleaseType(): ReleaseTypeEnumModel = when(this) {
        ReleaseTypeEnum.OAD -> ReleaseTypeEnumModel.OAD
        ReleaseTypeEnum.ONA -> ReleaseTypeEnumModel.ONA
        ReleaseTypeEnum.OVA -> ReleaseTypeEnumModel.OVA
        ReleaseTypeEnum.WEB -> ReleaseTypeEnumModel.WEB
        ReleaseTypeEnum.DORAMA -> ReleaseTypeEnumModel.DORAMA
        ReleaseTypeEnum.SPECIAL -> ReleaseTypeEnumModel.SPECIAL
        ReleaseTypeEnum.MOVIE -> ReleaseTypeEnumModel.MOVIE
        ReleaseTypeEnum.TV -> ReleaseTypeEnumModel.TV
    }
}