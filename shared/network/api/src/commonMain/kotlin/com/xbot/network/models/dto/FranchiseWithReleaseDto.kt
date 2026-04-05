package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FranchiseWithReleaseDto(
    @SerialName("id") val id: String,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("release_id") val releaseId: Int,
    @SerialName("franchise_id") val franchiseId: String,
    @SerialName("release") val release: ReleaseDto,
)